# Introduction
This code base is intended to demonstrate building custom reactors. 

# Please follow our project git setup
add the hook dir into git config
```
git config core.hooksPath .git/hooks
```

create the file commit-msg in .git/hooks/
```
#!/usr/bin/env bash

# Create a regex for a conventional commit.
convetional_commit_regex="^(build|chore|ci|docs|feat|fix|perf|refactor|revert|style|test)(\([a-z \-]+\))?!?: .+$"

# Get the commit message (the parameter we're given is just the path to the
# temporary file which holds the message).
commit_message=$(cat "$1")

# Check the message, if we match, all good baby.
if [[ "$commit_message" =~ $convetional_commit_regex ]]; then
   echo -e "\e[32mCommit message meets Conventional Commit standards...\e[0m"
   exit 0
fi

# Uh-oh, this is not a conventional commit, show an example and link to the spec.
echo -e "\e[31mThe commit message does not meet the Conventional Commit standard\e[0m"
echo "An example of a valid message is: "
echo "  feat(login): add the 'remember me' button"
echo "More details at: https://www.conventionalcommits.org/en/v1.0.0/#summary"
exit 1
```



# Set Up
To run the reactors in your environment:
- Place the Java classes inside your project directory. The location for files is: `PROJECT_ROOT/version/assets/java`
- Run the following commands in the pixel console
```
SetContext("<your-project-id>");
ReloadInsightClasses ( "<your-project-id>");
```