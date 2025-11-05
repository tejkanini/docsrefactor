# What are Reactors?

Reactors are the business logic units of Pixel which perform the desired operation for the user


## How to make a Reactor
New reactors must implement `IReactor`. `AbstractReactor` implements `IReactor` and provides all the scaffolding required for most of the reactors that are needed. The only method that needs to be implemented is the `execute()` method.


##  Example

```
package prerna.sablecc2.reactor;
import prerna.sablecc2.om.NounMetadata;

public class NewReactor extends AbstractReactor {

    @Override
    public NounMetadata execute() {
        System.out.println("Hello World!");
        return null;
    }
}

```

Reference the [src/reactors](https://repo.semoss.org/semoss-training/backend/src/reactors) folder for more examples.