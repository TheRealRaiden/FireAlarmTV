module firealarmtv {
    requires javafx.controls;
    requires transitive javafx.graphics;
    requires transitive javafx.web;
    requires transitive com.gluonhq.maps;
	requires com.fasterxml.jackson.databind;
	requires transitive json.path;
	requires org.slf4j;
	requires spring.messaging;
    exports ch.firealarmtv.application;
}
