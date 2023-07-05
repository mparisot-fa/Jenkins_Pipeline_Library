import hudson.model.User
def call(Map config) {
    currentUser = User.current()

    roles = ["dev", "devops", "liveops", "nobody"]
    println(roles)
    role = roles[new Random().nextInt(roles.size())]
    println("current role " + role)

    environments =  ['sandbox', 'dev']
    switch(role){
        case "dev":
            break;
        case "devops":
            environments.add("qa")
            break;
        case "liveops":
            environments.add("stg")
            environments.add("qa")
            break;
        default:
            println("Invalid role [" + role + "], aborting.")
            throw Exception("too bad")
            break
    }


    //config["environments"]

    default_parameters = [
            string(
                    defaultValue: 'develop',
                    description: '<b>select the desired branch and please make sure that the branch name matches with git branch.</b><br><br>',
                    name: 'Branch'
            ),
            choice(
                    choices: config["components"].keySet().sort(),
                    name: 'Component',
                    description: '''<b>Select the component for which you wish to build infrastructure for.</b><br><br>'''
            ),
            choice(
                    choices: ['No', 'Yes'],
                    description: '''<b>Yes - Pipeline will automatically get approved, build infrastructure and deploy application.&nbsp;No  - Pipeline will wait for you to verify infrastructure plan; you still get a chance to abort at this point</b><br><br>''',
                    name: 'AutoApproved'
            ),
            choice(
                    choices: environments,
                    name: 'Environment'
            )

    ]
pipeline_parameters = default_parameters

if(config.containsKey("extra_parameters"))
    pipeline_parameters.add(config["extra_parameters"])

    properties([
            parameters(pipeline_parameters)
    ])
}
