# How to call another Reactor from a Reactor?
**Add the [Movies Database](https://repo.semoss.org/semoss-training/backend/-/tree/master/db) and [CallHardQueryReactor.java](https://repo.semoss.org/semoss-training/backend/-/blob/master/src/reactors/CallHardQueryReactor.java) to follow along with this tutorial.**<br>
This tutorial assumes you already have knowledge on how to create a functioning reactor. If you need a refresher on how to create a reactor, please reference [Reactor.md](https://repo.semoss.org/semoss-training/backend/-/blob/master/tutorials/Reactor.md)

##  Creating the Reactor
First we need to determine user inputs and specify these as Keys. In this example we will require that the user provides the database Id

```
	public CallHardQueryReactor() {
		this.keysToGet = new String[] { ReactorKeysEnum.DATABASE.getKey() };
	}
```

##  Override execute() method
Just like creating any other reactor, we will override the execute() method from `AbstractReactor`. We want to end this method with returning the NounMetadata results from the reactor we called here, so it also gets returned to the user when this reactor is used.

```
	@Override
	public NounMetadata execute() {
		organizeKeys();
		String databaseId = this.keyValue.get(this.keysToGet[0]);

		return callHardQueryReactor(databaseId, this.insight);
	}
```

##  Example to call another reactor
To make the secondary reactor call, we first need to instantiate the reactor class we want to call. Next, for each reactor key we want to pass in, we have to create a new GenRowStruct and add metadata for it like this example. Lastly, we set the insight and run the Reactor's Execute method directly.

```
	private static NounMetadata callHardQueryReactor(String databaseId, Insight insight) {
		HardQueryReactor hardQueryReactor = new HardQueryReactor();
		hardQueryReactor.In();

		// 1 GenRowStruct per Reactor Key you are passing to the Reactor you want to call.
		GenRowStruct grs1 = new GenRowStruct();
		grs1.add(new NounMetadata(databaseId, PixelDataType.CONST_STRING));
		hardQueryReactor.getNounStore().addNoun(ReactorKeysEnum.DATABASE.getKey(), grs1);

		hardQueryReactor.setInsight(insight);
		NounMetadata retNoun = hardQueryReactor.execute();
		return retNoun;
	}

```

Reference the [src/reactors](https://repo.semoss.org/semoss-training/backend/src/reactors) folder for more examples.