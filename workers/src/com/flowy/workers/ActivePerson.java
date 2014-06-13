package com.flowy.workers;

import com.flowy.workers.db.Person;

public interface ActivePerson {
	public Person parse(String code);
}
