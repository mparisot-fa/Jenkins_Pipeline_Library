f = [:]
f["name"] = "super"
f["components"] = [ "s3", "apig","lambda" ]
f["config"] = [ "conf1": [ "name": "name1", "id": 42], "conf2": ["name": "name2", "id":43]]
println(f)
println("super="+f.components)
println("top="+f.get("name"))
for (component in f["components"]) {
    println("component=" + component)
}

for (key in f.keySet().sort()){
    println("f["+key + "]=" + f[key])
}

for (conf in f["config"].keySet().sort()){
    println("conf=" + conf + " name="+ f["config"][conf]["name"])
}

config = [:]
config["components"] = [
                "s3": [],
                "apig": [],
                "lambda": [ [ "name": "lambda1", "repo": "repo_uri", "module": "derived_domains", "type":"python", "params": [:]]
                ]
        ]

config["environments"] =  ['sandbox', 'dev', 'qa', 'stg', 'prod']
for (conf in config.keySet().sort()){
    println("conf="+conf)
}
for (component in config["components"].keySet().sort()){
    println("component=" + component)
}

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

println("current envs for role " + environments)
