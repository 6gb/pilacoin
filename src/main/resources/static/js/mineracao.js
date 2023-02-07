const scm = new StompJs.Client({
    brokerURL: "ws://srv-ceesp.proj.ufsm.br:8097/websocket/websocket",
    connectHeaders: { login: "pilacoin", passcode: "pilacoin"},
    debug: function (str) { console.log(str); },
    reconnectDelay: 5000, heartbeatIncoming: 4000, heartbeatOutgoing: 4000,
})

scm.onConnect = () => scm.subscribe("/topic/validaMineracao", (res) => {
    let pila = JSON.parse(res.body)
    if (pila.chaveCriador === carteira.chavePublica) adicionarLinha(pila)
})

scm.onStompError = frame => console.log("Erro: " + frame.headers["message"] + "\nDetalhes: " + frame.body);

let carteira;

(fetch("http://localhost:8080/carteira")
    .then(response => response.text())
    .then(data => carteira = JSON.parse(data))
    .catch(error => console.error(error))
).then(() => scm.activate())

function adicionarLinha(pila) {
    let tr = document.createElement("tr")
    let tds = [(new Date(pila.dataCriacao)).toLocaleString(), pila.nonce]
    console.log(pila.dataCriacao)
    console.log(pila.nonce)
    tds.forEach(innerText => {
        let td = document.createElement("td")
        td.innerText = innerText
        tr.appendChild(td)
    })
    document.getElementById("mineracao").appendChild(tr)
}
