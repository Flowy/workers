package com.example.workers;

public interface UnitOfWork {
	void commit();
	UnitOfWork startTransaction(Transaction transaction);
	public interface Transaction {
		void execute();
	}
}
