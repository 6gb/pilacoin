let blocos = [], usuarios = [];
(fetch("http://srv-ceesp.proj.ufsm.br:8097/bloco/all")
    .then(response => response.text())
    .then(data => blocos = JSON.parse(data))
    .catch(error => console.error(error))
).then(() => fetch("http://srv-ceesp.proj.ufsm.br:8097/usuario/all")
    .then(response => response.text())
    .then(data => usuarios = JSON.parse(data))
    .catch(error => console.error(error))
).then(() => montarTabelas(blocos, usuarios))
function montarTabelas(blocos, usuarios) {
    for (let bloco of blocos) {
        let usuarioMinerador = usuarios.find(usuario => usuario.chavePublica === bloco.chaveUsuarioMinerador)
        let table = document.createElement("table")
        let caption = document.createElement("caption")
        caption.innerText =
            `Transações do bloco núm. ${bloco.numeroBloco} (Usuário minerador: ${usuarioMinerador.nome})`
        table.appendChild(caption)
        let tr = document.createElement("tr")
        let ths = ["Usuário destinatário", "Data da transação", "Nonce do PilaCoin"]
        ths.forEach(innerText => {
            let th = document.createElement("th")
            th.innerText = innerText
            tr.appendChild(th)
        })
        table.appendChild(tr)
        for (let transacao of bloco.transacoes) {
            let usuarioDestino = usuarios.find(usuario => usuario.chavePublica === transacao.chaveUsuarioDestino)
            tr = document.createElement("tr")
            let tds = [usuarioDestino.nome, (new Date(transacao.dataTransacao)).toLocaleString(), transacao.noncePila]
            tds.forEach(innerText => {
                let td = document.createElement("td")
                td.innerText = innerText
                tr.appendChild(td)
            })
            table.appendChild(tr)
        }
        document.getElementById("blocos").appendChild(table)
    }
}
