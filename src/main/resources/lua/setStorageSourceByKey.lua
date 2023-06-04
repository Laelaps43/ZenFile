redis.call('DEL', KEYS[1])
redis.call('DEL', KEYS[2])
redis.call('set', KEYS[2], ARGV[1])
redis.call('set', KEYS[1], ARGV[2])
