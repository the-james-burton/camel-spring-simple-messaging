# camel-spring-simple-messaging [![Build Status](https://api.travis-ci.org/the-james-burton/camel-spring-simple-messaging.svg?branch=master)](https://travis-ci.org/the-james-burton/camel-spring-simple-messaging)

### Camel spring simple messaging component

This is a component for apache camel that lets you send messages to an implementation of Spring's `SimpMessageSendingOperations` such as `SimpMessagingTemplate`.

This is useful when you want to send (for example) STOMP messages over websockets using [Spring](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/websocket.html) when using Camel routes.

 > This is a producer-only component. Consuming messages is not currently supported.

### How to get it

It is available in maven central repo here...

```xml
<dependency>
    <groupId>org.jimsey.projects.camel.components</groupId>
    <artifactId>camel-spring-simple-messaging</artifactId>
    <version>1.0.0</version>
</dependency>
```

### How to use it

A sample `configure` method implementation in your Camel `RouteBuilder` might look like the below. Your implementation of `AbstractWebSocketMessageBrokerConfigurer` will inject the `SimpMessagingTemplate`.

```java
@Autowired
private SimpMessagingTemplate template;

@Override
public void configure() throws Exception {

  getContext().addComponent("ssm", new SpringSimpleMessagingComponent(template));

  from("direct:your.message.source")
  .to("ssm://topic/your.topic.name")
  .end();
}
```

This component will use the camel exchange `in` body as the `SimpMessagingTemplate` payload. There are no special requirements for the payload beyond simply being a java `Object`.

This component will also pass through all headers, including Camel headers except for the control headers documented below.

### URI format

```
<name>://destinaton
```

Where `<name>` is the name you gave it when you added the component and `destination` is where you want to send it to via the Spring `SimpMessageSendingOperations.convertAndSend` methods. See the options below for how to customise the destination dynamically, via a header.

### Options

There are no URI options in this component.

### Headers

These messaging control headers are removed before sending to the `SimpMessagingTemplate`. All other headers will remain on the message.

Header | Value
--- | ---
`SpringSimpleMessagingConstants.DESTINATION_SUFFIX` | An optional suffix to add to the destination. Useful to control message routing dynamically.
`SpringSimpleMessagingConstants.USER` | If present, the component will call the `convertAndSendToUser` message on the `SimpMessagingTemplate` with the `user` parameter set to the value of this header. If not present then the `convertAndSend` method will be used instead, which does not require a `user` parameter.

### How to build

Run `mvn install`

### How to contribute

The [Spring API](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/messaging/simp/SimpMessageSendingOperations.html) for this is very small, so I don't expect much needs to change for a while. However, anyone is more than welcome to submit a pull request and I will check it, merge it in and release it to maven central. 

I am also happy to add additional contributors who submit more than one pull request and/or have bigger plans for this component than I do.
