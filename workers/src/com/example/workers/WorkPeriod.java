package com.example.workers;

import java.util.Date;

class WorkPeriod {
	Date periodStart;
	DayTime[] times;
	
	WorkPeriod(int periodDuration) {
		times = new DayTime[periodDuration];
	}
	
	int getPeriodDuration() {
		return times.length;
	}
	
}
