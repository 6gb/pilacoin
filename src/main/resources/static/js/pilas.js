let pilas = [], carteira;
let carregando;

function mostrarCarregando() {
    let p = document.createElement("p")
    p.innerText = "Carregando..."
    carregando = true
    document.getElementById("pilas").appendChild(p);
    carregando = setInterval(() => p.innerText += ".", 1000)
}

function listarPilas() {
    mostrarCarregando();
    (fetch("http://localhost:8080/carteira")
            .then(response => response.text())
            .then(data => carteira = JSON.parse(data))
            .catch(error => console.error(error))
    ).then(() => fetch("http://srv-ceesp.proj.ufsm.br:8097/pilacoin/all")
        .then(response => response.text())
        .then(data => pilas = JSON.parse(data))
        .catch(error => console.error(error))
    ).then(() => montarTabela(pilas, carteira))
}

function montarTabela(pilas, carteira) {
    clearInterval(carregando);
    document.getElementById("pilas").firstChild.remove()
    let table = document.createElement("table")
    let caption = document.createElement("caption")
    caption.innerText = `PilaCoins minerados (Usuário: ${carteira.usuario})`
    table.appendChild(caption)
    let tr = document.createElement("tr")
    let ths = ["ID", "Data e hora da mineração", "Nonce", "Status"]

    ths.forEach(innerText => {
        let th = document.createElement("th")
        th.innerText = innerText
        tr.appendChild(th)
    })

    table.appendChild(tr)

    for (let pila of pilas) {
        if (pila.chaveCriador !== carteira.chavePublica) continue
        tr = document.createElement("tr")
        let tds = [pila.id, (new Date(pila.dataCriacao)).toLocaleString(), pila.nonce, pila.status]
        tds.forEach(innerText => {
            let td = document.createElement("td")
            td.innerText = innerText
            tr.appendChild(td)
        })
        table.appendChild(tr)
    }
    document.getElementById("pilas").appendChild(table)
}
