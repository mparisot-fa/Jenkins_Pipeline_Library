def call(Map config) {

    properties([
            parameters([
                    string(
                            defaultValue: 'develop',
                            description: '<b>select the desired branch and please make sure that the branch name matches with git branch.</b><br><br>',
                            name: 'Branch'
                    ),
                    choice(
                            choices: ['s3', 'glue', 'secrets'],
                            name: 'Component',
                            description: '''<b>Select the component for which you wish to build infrastructure for.</b><br><br>'''
                    ),
                    choice(
                            choices: ['No', 'Yes'],
                            description: '''<b>Yes - Pipeline will automatically get approved, build infrastructure and deploy application.&nbsp;No  - Pipeline will wait for you to verify infrastructure plan; you still get a chance to abort at this point</b><br><br>''',
                            name: 'AutoApproved'
                    ),
                    choice(
                            choices: config["environments"],
                            name: 'Environment'
                    )
            ])
    ])
}
