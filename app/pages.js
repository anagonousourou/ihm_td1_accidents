
function indexroute(req, res) {
  res.sendFile('templates/index.html', { root: __dirname })
}

module.exports = { indexroute }
