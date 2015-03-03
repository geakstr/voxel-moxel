package me.geakstr.voxel.helpers;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class BidirectionalMap<KeyType, ValueType> {
    private Map<KeyType, ValueType> keyToValueMap = new LinkedHashMap<KeyType, ValueType>();
    private Map<ValueType, KeyType> valueToKeyMap = new LinkedHashMap<ValueType, KeyType>();

    public void put(KeyType key, ValueType value){
        keyToValueMap.put(key, value);
        valueToKeyMap.put(value, key);
    }

    public ValueType removeByKey(KeyType key){
        ValueType removedValue = keyToValueMap.remove(key);
        valueToKeyMap.remove(removedValue);
        return removedValue;
    }

    public KeyType removeByValue(ValueType value){
        KeyType removedKey = valueToKeyMap.remove(value);
        keyToValueMap.remove(removedKey);
        return removedKey;
    }

    public boolean containsKey(KeyType key){
        return keyToValueMap.containsKey(key);
    }

    public boolean containsValue(ValueType value){
        return valueToKeyMap.containsKey(value);
    }

    public KeyType getKey(ValueType value){
        return valueToKeyMap.get(value);
    }

    public ValueType get(KeyType key){
        return keyToValueMap.get(key);
    }
    
    public Set<Map.Entry<KeyType, ValueType>> keyToValueEntrySet() {
    	return keyToValueMap.entrySet();
    }
    
    public int size() {
    	return keyToValueMap.size();
    }
}