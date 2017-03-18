package com.imadcn.demo.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(FIELD)
@Retention(RUNTIME)
@Documented
public @interface Overwatch {
	
	String name();
	HEROES hero();
	
	public enum HEROES {Genji, Mccree, Pharah, Reaper, Soilder76, Sombra, Tracer, Bastion, Hanzo, Junkrat, Mei, Torbjon, Widowmaker, Dva, Orisa, Reinhardt, Roadhog, Winston, Zarya, Anna, Lucio, Mercy, Symmetra, Zenyatta};
}
