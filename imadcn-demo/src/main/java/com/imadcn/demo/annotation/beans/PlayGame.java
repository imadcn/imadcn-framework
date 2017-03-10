package com.imadcn.demo.annotation.beans;

import com.imadcn.demo.annotation.Overwatch;
import com.imadcn.demo.annotation.Overwatch.HEROES;

public class PlayGame {
	
	@Overwatch(name="Genji", hero=HEROES.Genji)
	private String avartar;

}
