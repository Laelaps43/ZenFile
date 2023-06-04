local tmp = redis.call('get', KEYS[1])
if( tmp == false )then
    return nil
else
    return redis.call('get', KEYS[2] .. tmp)
end