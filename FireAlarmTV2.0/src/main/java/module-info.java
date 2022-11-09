module firealarmtv {
	exports ch.firealarmtv.service;
	exports ch.firealarmtv.config;
	exports ch.firealarmtv.model;
	exports ch.firealarmtv.application;
	exports ch.firealarmtv.map;
	exports ch.firealarmtv.handlers;
	exports ch.firealarmtv.controller;

	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.databind;
	requires com.gluonhq.maps;
	requires java.xml;
	requires javafx.base;
	requires javafx.controls;
	requires javafx.graphics;
	requires json.path;
	requires org.slf4j;
	requires spring.messaging;
}