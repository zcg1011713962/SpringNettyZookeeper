一、zookeeper集群搭建(docker-compose方式)
  1、拉取镜像 zookeeper:3.5.6
  2、创建docker-compose.yml 内容如下：
	
    version: '3'
    services:
        zoo1:
            image: zookeeper:3.5.6
            restart: always
            hostname: zoo1
            ports:
                - 2181:2181
            environment:
                ZOO_MY_ID: 1
                ZOO_SERVERS: server.1=zoo1:2888:3888;2181 server.2=zoo2:2888:3888;2181 server.3=zoo3:2888:3888;2181

        zoo2:
            image: zookeeper:3.5.6
            restart: always
            hostname: zoo2
            ports:
                - 2182:2181
            environment:
                ZOO_MY_ID: 2
                ZOO_SERVERS: server.1=zoo1:2888:3888;2181 server.2=zoo2:2888:3888;2181 server.3=zoo3:2888:3888;2181

        zoo3:
            image: zookeeper:3.5.6
            restart: always
            hostname: zoo3
            ports:
                - 2183:2181
            environment:
                ZOO_MY_ID: 3
                ZOO_SERVERS: server.1=zoo1:2888:3888;2181 server.2=zoo2:2888:3888;2181 server.3=zoo3:2888:3888;2181
                
 3、启动镜像 docker-compose up -d 
 
二、server使用curator注册netty服务
        ZookeeperClient zookeeperClient = new ZookeeperClient(CommUtil.getZookeeperServerList(),5000,5000);
三、client使用curator拉取netty服务
        ZookeeperClient zookeeperClient = new ZookeeperClient(CommUtil.getZookeeperServerList(),5000,5000);
        list = zookeeperClient.getNode(nodeName);
