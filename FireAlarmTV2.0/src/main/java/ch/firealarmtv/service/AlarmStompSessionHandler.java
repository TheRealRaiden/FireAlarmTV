package ch.firealarmtv.service;

import java.lang.reflect.Type;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import ch.firealarmtv.application.App;
import ch.firealarmtv.config.PropertiesConfigHandler;
import ch.firealarmtv.objects.Alarm;


public class AlarmStompSessionHandler extends StompSessionHandlerAdapter{
	
	private App app;
	private PropertiesConfigHandler config;


	/**
	 * @param app
	 * @param config
	 */
	public AlarmStompSessionHandler(App app, PropertiesConfigHandler config) {
		super();
		this.app = app;
		this.config = config;
	}

	@Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        System.out.println("New session established : " + session.getSessionId());
        session.subscribe("/topic/alarms/get", this);
        System.out.println("Subscribed to /topic/alarms/get");
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        System.out.println("Got exception: " + exception.getMessage());
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return Alarm.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {

        System.out.print("Received alarm: ");
        Alarm alarm = (Alarm) payload;
        System.out.println(alarm.getType()+" - "+alarm.getCity()+" - "+alarm.getAddress());

    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {

        System.out.println("Transport Error! " + exception.getMessage());
        exception.printStackTrace();

    }

}
