package com.mars.framework.datasource.cache;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.mars.framework.datasource.cache.RelativeCacheContext.*;

@Slf4j
public class RelativeCache implements Cache {

    private Map<Object, Object> CACHE_MAP = new ConcurrentHashMap<>();

    private List<RelativeCache> relations = new ArrayList<>();

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);

    private String id;
    private Class<?> mapperClass;
    private boolean clearing;

    public RelativeCache(String id) throws Exception {
        this.id = id;
        this.mapperClass = Class.forName(id);
        RelativeCacheContext.putCache(mapperClass, this);
        loadRelations();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void putObject(Object key, Object value) {
        CACHE_MAP.put(key, value);
    }

    @Override
    public Object getObject(Object key) {
        return CACHE_MAP.get(key);
    }

    @Override
    public Object removeObject(Object key) {
        return CACHE_MAP.remove(key);
    }

    @Override
    public void clear() {
        ReadWriteLock readWriteLock = getReadWriteLock();
        Lock lock = readWriteLock.writeLock();
        lock.lock();
        try {
            // 判断 当前缓存是否正在清空，如果正在清空，取消本次操作
            // 避免缓存出现 循环 relation，造成递归无终止，调用栈溢出
            if (clearing) {
                return;
            }
            clearing = true;
            try {
                CACHE_MAP.clear();
                relations.forEach(RelativeCache::clear);
            } finally {
                clearing = false;
            }
        } finally {
            lock.unlock();
        }


    }

    @Override
    public int getSize() {
        return CACHE_MAP.size();
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

    public void addRelation(RelativeCache relation) {
        if (relations.contains(relation)){
            return;
        }
        relations.add(relation);
    }

    void loadRelations() {
        // 加载 其他缓存更新时 需要更新此缓存的 caches
        // 将 此缓存 加入至这些 caches 的 relations 中
        List<RelativeCache> to = UN_LOAD_TO_RELATIVE_CACHES_MAP.get(mapperClass);
        if (to != null) {
            to.forEach(relativeCache -> this.addRelation(relativeCache));
        }
        // 加载 此缓存更新时 需要更新的一些缓存 caches
        // 将这些缓存 caches 加入 至 此缓存 relations 中
        List<RelativeCache> from = UN_LOAD_FROM_RELATIVE_CACHES_MAP.get(mapperClass);
        if (from != null) {
            from.forEach(relativeCache -> relativeCache.addRelation(this));
        }

        CacheRelations annotation = AnnotationUtils.findAnnotation(mapperClass, CacheRelations.class);
        if (annotation == null) {
            return;
        }

        Class<?>[] toMappers = annotation.to();
        Class<?>[] fromMappers = annotation.from();

        if (toMappers != null && toMappers.length > 0) {
            for (Class c : toMappers) {
                RelativeCache relativeCache = MAPPER_CACHE_MAP.get(c);
                if (relativeCache != null) {
                    // 将找到的缓存添加到当前缓存的relations中
                    this.addRelation(relativeCache);
                } else {
                    // 如果找不到 to cache，证明to cache还未加载，这时需将对应关系存放到 UN_LOAD_FROM_RELATIVE_CACHES_MAP
                    // 也就是说 c 对应的 cache 需要 在 当前缓存更新时 进行更新
                    List<RelativeCache> relativeCaches = UN_LOAD_FROM_RELATIVE_CACHES_MAP.computeIfAbsent(c, t -> new ArrayList<RelativeCache>());
                    relativeCaches.add(this);
                }
            }
        }

        if (fromMappers != null && fromMappers.length > 0) {
            for (Class c : fromMappers) {
                RelativeCache relativeCache = MAPPER_CACHE_MAP.get(c);
                if (relativeCache != null) {
                    // 将找到的缓存添加到当前缓存的relations中
                    relativeCache.addRelation(this);
                } else {
                    // 如果找不到 from cache，证明from cache还未加载，这时需将对应关系存放到 UN_LOAD_TO_RELATIVE_CACHES_MAP
                    // 也就是说 c 对应的 cache 更新时需要更新当前缓存
                    List<RelativeCache> relativeCaches = UN_LOAD_TO_RELATIVE_CACHES_MAP.computeIfAbsent(c, t -> new ArrayList<RelativeCache>());
                    relativeCaches.add(this);
                }
            }
        }
    }

}
