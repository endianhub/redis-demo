# 分享知识  传递快乐 

<br>

 初学redis，分别用RedisTemplate和JedisPool做的一个小小的demo，

### Redis与spring的整合一般分为spring-data-redis整合和JedisPool整合，先看看两者的区别:

**1)、引用的依赖不同：**

spring-data-redis使用的依赖如下：

```
<dependency>  
    <groupId>org.springframework.data</groupId>  
    <artifactId>spring-data-redis</artifactId>  
    <version>1.0.2.RELEASE</version>  
</dependency>
```

JedisPool使用的依赖如下：

```
<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
    <version>2.7.2</version>
    <type>jar</type>
    <scope>compile</scope>
</dependency>
```





**2)、管理Jedis实例方式、操作redis服务的不同：**

**spring-data-redis：**

通过org.springframework.data.redis.connection.jedis.JedisConnectionFactory来管理，即通过工厂类管理，然后通过配置的模版bean，操作redis服务，代码段中充斥大量与业务无关的模版片段代码，代码冗余，不易维护，比如像下面的代码：

```
@Autowired
protected RedisTemplate<Serializable, Serializable> redisTemplate;

public void saveUser(final User user) {
    redisTemplate.execute(new RedisCallback<Object>() {

        @Override
        public Object doInRedis(RedisConnection connection) throws DataAccessException {
            connection.set(redisTemplate.getStringSerializer().serialize("user.uid." + user.getId()),
                           redisTemplate.getStringSerializer().serialize(user.getName()));
            return null;
        }
    });
}


@Override
public User getUser(final long id) {
    return redisTemplate.execute(new RedisCallback<User>() {
        @Override
        public User doInRedis(RedisConnection connection) throws DataAccessException {
            byte[] key = redisTemplate.getStringSerializer().serialize("user.uid." + id);
            if (connection.exists(key)) {
                byte[] value = connection.get(key);
                String name = redisTemplate.getStringSerializer().deserialize(value);
                User user = new User();
                user.setName(name);
                user.setId(id);
                return user;
            }
            return null;
        }
    });
}

```

**RedisTemplate介绍**

spring 封装了 RedisTemplate 对象来进行对redis的各种操作，它支持所有的 redis 原生的 api。在RedisTemplate中提供了几个常用的接口方法的使用，分别是:

```
private ValueOperations<K, V> valueOps;
private ListOperations<K, V> listOps;
private SetOperations<K, V> setOps;
private ZSetOperations<K, V> zSetOps;
```


RedisTemplate中定义了对5种数据结构操作

```
redisTemplate.opsForValue();//操作字符串  
redisTemplate.opsForHash();//操作hash  
redisTemplate.opsForList();//操作list  
redisTemplate.opsForSet();//操作set  
redisTemplate.opsForZSet();//操作有序set 
```


StringRedisTemplate与RedisTemplate 

两者的关系是StringRedisTemplate继承RedisTemplate。<br>
两者的数据是不共通的；也就是说StringRedisTemplate只能管理StringRedisTemplate里面的数据，RedisTemplate只能管理RedisTemplate中的数据。<br>
SDR默认采用的序列化策略有两种，一种是String的序列化策略，一种是JDK的序列化策略。<br>
StringRedisTemplate默认采用的是String的序列化策略，保存的key和value都是采用此策略序列化保存的。<br>

RedisTemplate默认采用的是JDK的序列化策略，保存的key和value都是采用此策略序列化保存的。

<br>
<br>
<br>


### jedis方式：
通过redis.clients.jedis.JedisPool来管理，即通过池来管理，通过池对象获取jedis实例，然后通过jedis实例直接操作redis服务，剔除了与业务无关的冗余代码，如下面的代码片段：

```
private JedisPool jedisPool;  
  
public String save(String key,String val) {  
    Jedis jedis = jedisPool.getResource();  
    return jedis.set(key, val);  
}
```

从工厂类到池的方式变化，就相当于mybatis连接mysql方变化是一样的，代码变得更简洁，维护也更容易了。Jedis使用apache commons-pool2对Jedis资源池进行管理，所以在定义JedisPool时一个很重要的参数就是资源池GenericObjectPoolConfig，使用方式如下，其中有很多资源管理和使用的参数。


**参数说明**
JedisPool保证资源在一个可控范围内，并且提供了线程安全，但是一个合理的GenericObjectPoolConfig配置能为应用使用Redis保驾护航，下面将对它的一些重要参数进行说明和建议：

在当前环境下，Jedis连接就是资源，JedisPool管理的就是Jedis连接。


**1. 资源设置和使用**

maxTotal：资源池中最大连接数；默认值：8	设置建议见下节<br>
maxIdle：资源池允许最大空闲的连接数；默认值：8；使用建议：设置建议见下节<br>
minIdle：资源池确保最少空闲的连接数；默认值：0；使用建议：设置建议见下节<br>
blockWhenExhausted：当资源池用尽后，调用者是否要等待。只有当为true时，下面的maxWaitMillis才会生效；默认值：true；使用建议：建议使用默认值<br>
maxWaitMillis：当资源池连接用尽后，调用者的最大等待时间(单位为毫秒)	-1：表示永不超时；使用建议：不建议使用默认值<br>
testOnBorrow：向资源池借用连接时是否做连接有效性检测(ping)，无效连接会被移除；默认值：false；使用建议：业务量很大时候建议设置为false(多一次ping的开销)。<br>
testOnReturn：向资源池归还连接时是否做连接有效性检测(ping)，无效连接会被移除；默认值：false；使用建议：业务量很大时候建议设置为false(多一次ping的开销)。<br>
jmxEnabled：是否开启jmx监控，可用于监控；默认值：true；使用建议：建议开启，但应用本身也要开启<br>


**2.空闲资源监测**

空闲Jedis对象检测，下面四个参数组合来完成，testWhileIdle是该功能的开关。

testWhileIdle：是否开启空闲资源监测；默认值：false；使用建议：true<br>
timeBetweenEvictionRunsMillis：空闲资源的检测周期(单位为毫秒)；默认值：-1：不检测；使用建议：建议设置，周期自行选择，也可以默认也可以使用下面JedisPoolConfig中的配置<br>
minEvictableIdleTimeMillis：资源池中资源最小空闲时间(单位为毫秒)，达到此值后空闲资源将被移除；默认值：1000 60 30 = 30分钟；使用建议：可根据自身业务决定，大部分默认值即可，也可以考虑使用下面JeidsPoolConfig中的配置<br>
numTestsPerEvictionRun：做空闲资源检测时，每次的采样数；默认值：3；使用建议：可根据自身应用连接数进行微调,如果设置为-1，就是对所有连接做空闲监测<br>


但是本文与spring集成并未直接采用JedisPool，而是采用了ShardedJedisPool，为什么呢？<br>
因为ShardedJedisPool可以通过一致性哈希实现分布式存储。<br>

shared一致性哈希采用以下方案：<br>
1、Redis服务器节点划分：将每台服务器节点采用hash算法划分为160个虚拟节点(可以配置划分权重)<br>
2、将划分虚拟节点采用TreeMap存储<br>
3、对每个Redis服务器的物理连接采用LinkedHashMap存储<br>
4、对Key or KeyTag 采用同样的hash算法，然后从TreeMap获取大于等于键hash值得节点，取最邻近节点存储；当key的hash值大于虚拟节点hash值得最大值时，存入第一个虚拟节点sharded采用的hash算法：MD5 和 MurmurHash两种；默认采用64位的MurmurHash算法；<br>


<br>
<br>
<br>


JedisSentinelPool和JedisPool区别<br>

JedisSentinelPool：（连接池）用于配置连接多个redis缓存数据库，分布式，多个redis同步数据时用<br>
JedisPool：（连接池）用户连接单个redis缓存数据库<br>



### 版本：
spring4.2.6
redis1.8.9









<br><br><br>

---

**如有不足或新的想法请留言--分享知识 传递快乐。** 