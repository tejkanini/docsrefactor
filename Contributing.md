# **Introduction**  

Welcome to the Contribution page !  

We’re excited to have you here! Contributing to the repository goes way beyond just coding. We are eager to receive your insights and feedback as well. Highlighting issues is an excellent way to share your ideas with the SEMOSS community. Issues not only serve as a log for bugs in both code and documentation, but also as a forum for community discussion. There might be other SEMOSS users who are seeking or proposing similar solutions.  

Issues are crucial for identifying flaws as the SEMOSS platform is still in its early stages. We anticipate that developers might encounter various challenges or bugs during the course of platform development. By providing detailed steps for reproduction, along with screenshots and thorough descriptions, you can make it easier for developers to understand and resolve these issues swiftly. The **XX** team is actively monitoring the GitHub issues list to manage and rectify problems as they arise.

For those who wish to contribute to SEMOSS platform, can do so by:  

* [Submitting Bug Report](#reporting-bugs)  
* [Submitting Feature Enhancement Request](#suggesting-feature-enhancements)  
* [Submitting Pull Request](#submitting-pull-requests)  
* [Submitting Documentation Request](#documentation-requests)

## How you can contribute:  

### Reporting Bugs   

Bug reports play a crucial role in maintaining and enhancing the quality of any software platform. Each report that a user submits acts as a fundamental feedback tool that informs the development team about the existence of an issue that might have otherwise gone unnoticed.  

For reporting a defect in the SEMOSS codebase. Use this to report an issue that is causing the code to:  
* Throw an unhandled error
* Not work as described
* Have a regression from the prior version, without any deprecation notice

Please avoid combining several bugs or requests into a single issue. Refrain from appending your issue as a comment to an existing one unless it relates to the exact same input. Even if issues appear similar, their underlying causes often differ. The more detailed information you provide, the higher the likelihood that someone will be able to reproduce the issue and identify a solution. When possible, please include the following with each issue:

* **Step-by-step Reproduction Guide:** Outline the steps taken (1, 2, 3, ...) and detail what you expected to happen compared to what actually occurred
* **Visual Aids:** Attach images, animations, or links to videos. While these visuals can help illustrate the steps, they should not replace the written step-by-step guide
* **Code Snippets or Repository Links:** If relevant, include a snippet of code that highlights the issue or provide a link to a repository that we can access and review to better understand and address the issue


**How to submit a bug report** 

Before logging any issue/bug, it's essential to first check the **GitHub issues** list to determine if a similar issue has already been reported. If an existing issue is found, contribute any additional information through a comment on that thread rather than creating a new issue.

If you cannot find an existing issue that describes your bug or feature, submit an issue using the guidelines below.   

1. Provide a clear and concise description of the bug/issue you're reporting.

2. Provide steps to reproduce to reproduce the issue in the following manner.
     a. Go to '...'
     b. Click on '....'
     c. Scroll down to '....'
     d. See error

* **Expected results**  
  Describe the expected result of action.  

* **Actual results**  
  Describe what actually happened.

* **Screenshots**  
  Please include add screenshots where applicable to help explain your problem. Please include the network tab if the issue pertains to pixel calls.  

* **Environment**  
Please complete the following information:  
    * browser type and version:
    * node.js version: `node -v`

* **Checklist**  
Make sure to follow these steps before submitting your issue - thank you!  

  * I have checked my terminal/console and have included relevant text here
  * I have reproduced the issue with at least the latest released version
  * I have checked the issue tracker for the same issue and I haven't found one similar

* **Additional context**  
Add any other context about the problem here.  


### Suggesting Feature Enhancements  

For introducing a new concept to SEMOSS platform use the steps given below. This should include a possible plan for implementation.

**How to propose new features or improvements**  

Before proposing a new feature or enhancement, it's essential to first check the **GitHub issues** list to determine if a similar request/suggestion has already been made. If an existing request for the feature is found, contribute any additional information through a comment on that thread rather than creating a new issue.

If you cannot find an existing issue that describes your feature, submit an issue using the guidelines below.

**Information to include in a feature request**  

* **Is your feature request related to a problem? Please describe.**

  A clear and concise description of what the problem is. Ex. I'm always frustrated when [...]

* **Describe the solution you'd like**

  A clear and concise description of what you want to happen.

* **Describe alternatives you've considered**

  A clear and concise description of any alternative solutions or features you've considered.

* **What Priority Level would you ascribe to this feature?**

  Is this a high priority feature that your team needs for the current sprint, can it be put into our engineering team's future sprint, or is it a nice to have.

* **Additional context**

  Add any other context or screenshots about the feature request here.

* **Request By**

  Please include the team that you are on, any relevant information so that the team can make sure to include engineers who may have further context for your use case.


### Submitting Pull Requests  

* **Steps to follow before submitting a pull request (e.g., checking for existing issues, discussing changes)**

  Before submitting a new pull request, it's essential to first check the **GitHub issues** list to determine if a similar request has already been made. If an existing pull request is found, contribute any additional information through a comment on that thread rather than creating a new request.

If you cannot find an existing request that describes your issue, submit the request using the guidelines below.
  
* **Branch naming conventions**

  *tbd*
  
* **How to write a clear and descriptive pull request**

  This assumes that you have already created a branch on your own fork with the changes you wish to submit. Please follow the steps to create a fork in you repository if you have not done it yet. 

1. Navigate to the [Pull Requests](https://github.com/Deloitte-Default/cfgai-docs/pulls) tab in the official GitHub repo. Here, click "New Pull Request" and you will see "compare across forks" on the screen.

2. Here, you will need to click on "compare across forks" to start seeing the branches on your fork. Select your fork in "Head repository" and your branch in "compare" for the source:

3. Now you can simply click "Create Pull Request" to start the review process. Do link the issue for which you're submitting pull request. Alternatively, you can also create a "Draft Pull Request" if the branch is still a work-in-progress.
  
* **Reviewing Pull Requests**

  Reviewing pull requests is another great way to contribute. The following filter is a useful tool for actively seeking out pull requests which have passed verification and need review:

  `is:pr is:open  -is:draft review:required status:success`

  Check 'main' branch to check if there have been any major changes, otherwise you need to re-raise the pull request.
  
* **Guidelines for writing tests and documentation**

  *tbd*

### Documentation Requests  

Our [documentation](https://workshop.cfg.deloitte.com/docs/) is refreshed nightly to provide the most current information. If you need documentation on a topic that is not currently covered, please follow the steps below:

**How to Submit a Documentation Request**

1. Post your question to our [Q&A Community Board](https://github.com/SEMOSS/community/discussions/categories/q-a). Please make sure to include the appropriate context, screenshots, design input, and any other details needed. The Documentation team monitors the board for documentation-related questions.

**How to Create an Issue from a Documentation Request (Steps for the Documentation Team)**

1. Click into the discussion question. Next, select 'Create issue from discussion' in the bottom right corner.
       <img width="314" alt="image" src="https://github.com/user-attachments/assets/3790a3d1-aa0f-47e5-8c0e-898aa4990bcd" />

2. Click into the 'Projects' dropdown and select 'Documentation' and 'SEMOSS'. Then click 'Create'.
     <img width="338" alt="image" src="https://github.com/user-attachments/assets/aa8bf10c-1fa4-4153-9dd8-8b0a2043f198" />

3. Once you create the issue, click 'Transfer Issue' and select the 'documentation' repo. Then select 'Transfer issue'.

4. Copy the link for the newly created issue and return to the orignal discussion question in the Q&A community board. Then, paste the link to the issue as a comment in the discussion question to promote tracking and visibility.
