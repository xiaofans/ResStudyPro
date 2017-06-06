package org.mapdb;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import org.mapdb.Atomic.Long;
import org.mapdb.Fun.Function2;
import org.mapdb.Fun.Tuple2;

public final class Bind {

    public interface MapListener<K, V> {
        void update(K k, V v, V v2);
    }

    public interface MapWithModificationListener<K, V> extends Map<K, V> {
        void modificationListenerAdd(MapListener<K, V> mapListener);

        void modificationListenerRemove(MapListener<K, V> mapListener);

        long sizeLong();
    }

    private Bind() {
    }

    public static <K, V> void size(MapWithModificationListener<K, V> map, final Long sizeCounter) {
        if (sizeCounter.get() == 0) {
            long size = map.sizeLong();
            if (sizeCounter.get() != size) {
                sizeCounter.set(size);
            }
        }
        map.modificationListenerAdd(new MapListener<K, V>() {
            public void update(K k, V oldVal, V newVal) {
                if (oldVal == null && newVal != null) {
                    sizeCounter.incrementAndGet();
                } else if (oldVal != null && newVal == null) {
                    sizeCounter.decrementAndGet();
                }
            }
        });
    }

    public static <K, V, V2> void secondaryValue(MapWithModificationListener<K, V> map, final Map<K, V2> secondary, final Function2<V2, K, V> fun) {
        if (secondary.isEmpty()) {
            for (Entry<K, V> e : map.entrySet()) {
                secondary.put(e.getKey(), fun.run(e.getKey(), e.getValue()));
            }
        }
        map.modificationListenerAdd(new MapListener<K, V>() {
            public void update(K key, V v, V newVal) {
                if (newVal == null) {
                    secondary.remove(key);
                } else {
                    secondary.put(key, fun.run(key, newVal));
                }
            }
        });
    }

    public static <K, V, V2> void secondaryValues(MapWithModificationListener<K, V> map, final Set<Tuple2<K, V2>> secondary, final Function2<V2[], K, V> fun) {
        if (secondary.isEmpty()) {
            for (Entry<K, V> e : map.entrySet()) {
                Object[] v = (Object[]) fun.run(e.getKey(), e.getValue());
                if (v != null) {
                    for (V2 v2 : v) {
                        secondary.add(Fun.t2(e.getKey(), v2));
                    }
                }
            }
        }
        map.modificationListenerAdd(new MapListener<K, V>() {
            public void update(K key, V oldVal, V newVal) {
                Object[] v;
                if (newVal == null) {
                    v = (Object[]) fun.run(key, oldVal);
                    if (v != null) {
                        for (V2 v2 : v) {
                            secondary.remove(Fun.t2(key, v2));
                        }
                    }
                } else if (oldVal == null) {
                    v = (Object[]) fun.run(key, newVal);
                    if (v != null) {
                        for (V2 v22 : v) {
                            secondary.add(Fun.t2(key, v22));
                        }
                    }
                } else {
                    Object[] oldv = (Object[]) fun.run(key, oldVal);
                    Object[] newv = (Object[]) fun.run(key, newVal);
                    if (oldv == null) {
                        if (newv != null) {
                            for (V2 v3 : newv) {
                                secondary.add(Fun.t2(key, v3));
                            }
                        }
                    } else if (newv == null) {
                        for (V2 v32 : oldv) {
                            secondary.remove(Fun.t2(key, v32));
                        }
                    } else {
                        Set<V2> hashes = new HashSet();
                        Collections.addAll(hashes, oldv);
                        for (V2 v322 : newv) {
                            if (!hashes.contains(v322)) {
                                secondary.add(Fun.t2(key, v322));
                            }
                        }
                        for (V2 v3222 : newv) {
                            hashes.remove(v3222);
                        }
                        for (V2 v32222 : hashes) {
                            secondary.remove(Fun.t2(key, v32222));
                        }
                    }
                }
            }
        });
    }

    public static <K, V, K2> void secondaryKey(MapWithModificationListener<K, V> map, final Set<Tuple2<K2, K>> secondary, final Function2<K2, K, V> fun) {
        if (secondary.isEmpty()) {
            for (Entry<K, V> e : map.entrySet()) {
                secondary.add(Fun.t2(fun.run(e.getKey(), e.getValue()), e.getKey()));
            }
        }
        map.modificationListenerAdd(new MapListener<K, V>() {
            public void update(K key, V oldVal, V newVal) {
                if (newVal == null) {
                    secondary.remove(Fun.t2(fun.run(key, oldVal), key));
                } else if (oldVal == null) {
                    secondary.add(Fun.t2(fun.run(key, newVal), key));
                } else {
                    K2 oldKey = fun.run(key, oldVal);
                    K2 newKey = fun.run(key, newVal);
                    if (oldKey != newKey && !oldKey.equals(newKey)) {
                        secondary.remove(Fun.t2(oldKey, key));
                        secondary.add(Fun.t2(newKey, key));
                    }
                }
            }
        });
    }

    public static <K, V, K2> void secondaryKey(MapWithModificationListener<K, V> map, final Map<K2, K> secondary, final Function2<K2, K, V> fun) {
        if (secondary.isEmpty()) {
            for (Entry<K, V> e : map.entrySet()) {
                secondary.put(fun.run(e.getKey(), e.getValue()), e.getKey());
            }
        }
        map.modificationListenerAdd(new MapListener<K, V>() {
            public void update(K key, V oldVal, V newVal) {
                if (newVal == null) {
                    secondary.remove(fun.run(key, oldVal));
                } else if (oldVal == null) {
                    secondary.put(fun.run(key, newVal), key);
                } else {
                    K2 oldKey = fun.run(key, oldVal);
                    K2 newKey = fun.run(key, newVal);
                    if (oldKey != newKey && !oldKey.equals(newKey)) {
                        secondary.remove(oldKey);
                        secondary.put(newKey, key);
                    }
                }
            }
        });
    }

    public static <K, V, K2> void secondaryKeys(MapWithModificationListener<K, V> map, final Set<Tuple2<K2, K>> secondary, final Function2<K2[], K, V> fun) {
        if (secondary.isEmpty()) {
            for (Entry<K, V> e : map.entrySet()) {
                Object[] k2 = (Object[]) fun.run(e.getKey(), e.getValue());
                if (k2 != null) {
                    for (K2 k22 : k2) {
                        secondary.add(Fun.t2(k22, e.getKey()));
                    }
                }
            }
        }
        map.modificationListenerAdd(new MapListener<K, V>() {
            public void update(K key, V oldVal, V newVal) {
                Object[] k2;
                if (newVal == null) {
                    k2 = (Object[]) fun.run(key, oldVal);
                    if (k2 != null) {
                        for (K2 k22 : k2) {
                            secondary.remove(Fun.t2(k22, key));
                        }
                    }
                } else if (oldVal == null) {
                    k2 = (Object[]) fun.run(key, newVal);
                    if (k2 != null) {
                        for (K2 k222 : k2) {
                            secondary.add(Fun.t2(k222, key));
                        }
                    }
                } else {
                    Object[] oldk = (Object[]) fun.run(key, oldVal);
                    Object[] newk = (Object[]) fun.run(key, newVal);
                    if (oldk == null) {
                        if (newk != null) {
                            for (K2 k2222 : newk) {
                                secondary.add(Fun.t2(k2222, key));
                            }
                        }
                    } else if (newk == null) {
                        for (K2 k22222 : oldk) {
                            secondary.remove(Fun.t2(k22222, key));
                        }
                    } else {
                        Set<K2> hashes = new HashSet();
                        Collections.addAll(hashes, oldk);
                        for (K2 k23 : newk) {
                            if (!hashes.contains(k23)) {
                                secondary.add(Fun.t2(k23, key));
                            }
                        }
                        for (K2 k232 : newk) {
                            hashes.remove(k232);
                        }
                        for (K2 k2322 : hashes) {
                            secondary.remove(Fun.t2(k2322, key));
                        }
                    }
                }
            }
        });
    }

    public static <K, V> void mapInverse(MapWithModificationListener<K, V> primary, Set<Tuple2<V, K>> inverse) {
        secondaryKey((MapWithModificationListener) primary, (Set) inverse, new Function2<V, K, V>() {
            public V run(K k, V value) {
                return value;
            }
        });
    }

    public static <K, V> void mapInverse(MapWithModificationListener<K, V> primary, Map<V, K> inverse) {
        secondaryKey((MapWithModificationListener) primary, (Map) inverse, new Function2<V, K, V>() {
            public V run(K k, V value) {
                return value;
            }
        });
    }

    public static <K, V, C> void histogram(MapWithModificationListener<K, V> primary, final ConcurrentMap<C, Long> histogram, final Function2<C, K, V> entryToCategory) {
        primary.modificationListenerAdd(new MapListener<K, V>() {
            public void update(K key, V oldVal, V newVal) {
                if (newVal == null) {
                    incrementHistogram(entryToCategory.run(key, oldVal), -1);
                } else if (oldVal == null) {
                    incrementHistogram(entryToCategory.run(key, newVal), 1);
                } else {
                    C oldCat = entryToCategory.run(key, oldVal);
                    C newCat = entryToCategory.run(key, newVal);
                    if (oldCat != newCat && !oldCat.equals(newCat)) {
                        incrementHistogram(oldCat, -1);
                        incrementHistogram(oldCat, 1);
                    }
                }
            }

            private void incrementHistogram(C category, long i) {
                while (true) {
                    Long oldCount = (Long) histogram.get(category);
                    if (oldCount != null) {
                        if (histogram.replace(category, oldCount, Long.valueOf(oldCount.longValue() + i))) {
                            return;
                        }
                    } else if (histogram.putIfAbsent(category, Long.valueOf(i)) == null) {
                        return;
                    }
                }
            }
        });
    }
}
