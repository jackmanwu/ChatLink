import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Created by JackManWu on 2018/1/29.
 */
public class JedisTest {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        List<String> list = jedis.lrange("wujinlei:xiaoming", 0, -1);
        System.out.println(list.size());
    }
}
