let records = document.getElementById("records");
let tableBody = document.getElementById("tableBody");
let alerting = document.getElementById("alerting");
let exporter = document.getElementById("exporter");

const requestOptions = {
  method: 'GET',
  headers: {
    'Content-Type': 'application/json'
  }
};

/* get all record from database */
/********************************/
records.addEventListener("click", () =>
{

  fetch('http://localhost:8080/api/house', requestOptions)
    .then(response => {

      if (!response.ok) {
        throw new Error('Erreur réseau lors de la requête !');
      }
      return response.json();
    })
    .then(data => {

      console.log('Données reçues :', data);
      let i = 1;

      for(let itemArray in data)
      {
        let tr = document.createElement("tr");
        let th = document.createElement("th");

        th.innerText = i;
        i = i + 1;

        tableBody.appendChild(tr);
        tableBody.appendChild(th);

        for(let item in data[itemArray])
        {
          if(item !== "url")
          {
            let th = document.createElement("th");
            if(data[itemArray][item] === null)
            {
              th.innerText = "NaN";
              tableBody.appendChild(th);
            }
            else
            {
              th.innerText = data[itemArray][item];
              tableBody.appendChild(th);
            }
          }
        }
      }

      createAlert("success", "Les valeurs sont bien récupèrées")

    })
    .catch(error => {

      console.error('Il y a eu une erreur :', error);
      createAlert("danger", "Une erreur s'est produite les valeurs ne sont pas disponibles")

    });

})


/* Exporter data in CSV format */
/*******************************/
exporter.addEventListener("click", () => {
  fetch('http://localhost:8080/api/house/export')
    .then(response => {
      if (!response.ok) {
        throw new Error('Erreur lors du téléchargement du fichier');
      }
      return response.blob(); // Récupère le contenu de la réponse comme un blob
    })
    .then(blob => {
      // Crée un lien pour télécharger le fichier
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', 'test.csv'); // Nom de fichier à télécharger
      document.body.appendChild(link);
      link.click();
    })
    .catch(error => {
      console.error('Erreur:', error);
    });

})

/* Alert function */
/******************/
function createAlert(color,message)
{
  alerting.classList.add("alert");
  alerting.classList.add("alert-" + color);
  alerting.textContent = message;
}
