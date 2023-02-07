const sc = new StompJs.Client({
    brokerURL: "ws://srv-ceesp.proj.ufsm.br:8097/websocket/websocket",
    connectHeaders: { login: "pilacoin", passcode: "pilacoin"},
    debug: function (str) { console.log(str); },
    reconnectDelay: 5000, heartbeatIncoming: 4000, heartbeatOutgoing: 4000,
})

const callback = res => document.getElementById("dificuldade").innerText =
    JSON.parse(res.body).dificuldade.length + " Fs (" + new Date().toLocaleString() + ")"
sc.onConnect = () => sc.subscribe("/topic/dificuldade", callback)
sc.onStompError = frame => console.log("Erro: " + frame.headers["message"] + "\nDetalhes: " + frame.body);
sc.activate();
