# Troubleshooting
## Git "remote: Repository not found" error
If you receive this error when attempting to git clone or other similar actions, your account may be in a conflicted state. An easy way to work around this is to use an OAuth token from a working account. You can follow GitHub's instructions to [create an OAuth key](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens) and [add it to your command line interface](https://docs.github.com/en/get-started/getting-started-with-git/caching-your-github-credentials-in-git). Alternately, the steps below will also allow you access.   
  
**Walkthrough**
1. Log into [GitHub](https://github.com/) on your working account. You'll want to make sure you can access the repository of interest.
2. Navigate to your [settings](https://github.com/settings/profile) page, then down to *Developer Settings...Personal Access Tokens...[Tokens (classic)](https://github.com/settings/tokens)*.
3. Generate new token (classic).
   1. Name it whatever you wish ("personal_full" or something similar works fine).
   2. Set the expiration date, if any.
   3. Add all permissions.
   4. Generate token.
4. Use the token to run your git command with the HTTPS url. For example, if you want to clone a repository, it would be...  
  ```
  git clone https://<oauth-token>@github.com/<username>/<repo>.git
  ```
  or  
  ```
  git clone https://<your-username>:<oauth-token>@github.com/<username>/<repo>.git
  ```
5. The command will throw an error, asking you to visit a webpage to confirm access.
6. Copy and paste the link into your browser, then sign-in and allow access for this OAuth token.
7. Run your git command again -- it should work this time!

**Note: You will need your OAuth token to run further commands. Also remember that your OAuth token grants access to your account and should always be kept secret.**
