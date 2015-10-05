# camel-spring-simple-messaging [![Build Status](https://api.travis-ci.org/the-james-burton/atacama.svg?branch=master)](https://travis-ci.org/the-james-burton/camel-spring-simple-messaging)

### Camel spring simple messaging component

This is a component for apache camel that lets you send messages to an implementation of Spring's `SimpMessageSendingOperations` such as `SimpMessagingTemplate`.

This is useful when you want to send (for example) STOMP messages over websockets using Spring (see Ch.21.4) when using Camel routes.

 > This is a producer-only component. Consuming messages is not currently supported.

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

### URI format

    <name>://destinaton

Where `<name>` is the name you gave it when you added the component and destination` is where you want to send it to via the Spring `SimpMessageSendingOperations.convertAndSend` methods. See the options below for how to customise the destination dynamically, via a header.

### Options

There are no URI options in this component.

### Headers

These messaging control headers are removed before sending to the `SimpMessagingTemplate`. All other headers will remain on the message.

Header | Value
--- | ---
SpringSimpleMessagingConstants.DESTINATION_SUFFIX | An optional suffix to add to the destination. Useful to control message routing dynamically.
SpringSimpleMessagingConstants.USER | If present, the component will call the `convertAndSendTouser` message on the `SimpMessagingTemplate`.


### How to build

Run `mvn install`
