/* The buttons */
/****************/
let submitBtn = document.getElementById("submitBtn");
let insertBtn = document.getElementById("insertBtn");
let inputFile = document.getElementById("inputFile");
let alerting = document.getElementById("alerting");

/* Data to fill */
/****************/
let price1 = document.getElementById("price1");
let price2 = document.getElementById("price2");
let city = document.getElementById("city");
let rooms = document.getElementById("rooms");
let dimension = document.getElementById("dimension");
let agency = document.getElementById("agency");
let constructYear = document.getElementById("constructYear");
let peb = document.getElementById("peb");
let type = document.getElementById("type");
let postalCode = document.getElementById("postalCode");


/* Data & structures */
/*********************/

const dataToInsert = {
  pricePrincipal : "",
  priceRent: "",
  city: "",
  nbrRooms: "",
  dimension: "",
  agency: "",
  constructYear: "",
  postalCode: "",
  peb: "",
  type: "",
  url: ""
};

const requestOptions = {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify(this.data)
};

let HouseObjet = {
  pricePrincipal : "",
  priceRent: "",
  city: "",
  nbrRooms: "",
  dimension: "",
  agency: "",
  constructYear: "",
  postalCode: "",
  peb: "",
  type: ""
}


/* get information with the url provide from web site  */
/*******************************************************/
submitBtn.addEventListener("click", () => {

  requestOptions.body = JSON.stringify(inputFile.value);
  console.log(requestOptions);

  fetch('http://localhost:8080/api/house', requestOptions)
    .then(response => {

      if (!response.ok) {
        throw new Error('Erreur réseau lors de la requête !');
      }

      return response.json();
    })
    .then(data => {

      console.log('Données reçues :', data);

      HouseObjet.pricePrincipal = data.pricePrincipal;
      HouseObjet.priceRent = data.princeRent;
      HouseObjet.city = data.city;
      HouseObjet.nbrRooms = data.nbrRooms;
      HouseObjet.dimension = data.dimension;
      HouseObjet.agency = data.agency;
      HouseObjet.peb = data.peb;
      HouseObjet.type = data.type;
      HouseObjet.constructYear = data.constructYear;
      HouseObjet.postalCode = data.postalCode;

      price1.value = data.pricePrincipal;
      price2.value = data.princeRent;
      city.value = data.city;
      rooms.value = data.nbrRooms;
      dimension.value = data.dimension;
      agency.value = data.agency;
      constructYear.value = data.constructYear;
      peb.value = data.peb;
      type.value = data.type;
      postalCode.value = data.postalCode;


      createAlert("success", "Les valeurs sont bien récupèrées")

    })
    .catch(error => {

      console.error('Il y a eu une erreur :', error);
      createAlert("danger", "Une erreur s'est produite les valeurs ne sont pas accéssibles")

    });

})


/* Insert data */
/**************/
insertBtn.addEventListener("click", (e) => {

  console.log("ok");
  dataToInsert.city=HouseObjet.city;
  dataToInsert.peb=HouseObjet.peb;
  dataToInsert.type=HouseObjet.type;
  dataToInsert.constructYear=HouseObjet.constructYear;
  dataToInsert.nbrRooms=HouseObjet.nbrRooms;
  dataToInsert.agency=HouseObjet.agency;
  dataToInsert.pricePrincipal=HouseObjet.pricePrincipal;
  dataToInsert.priceRent=HouseObjet.priceRent;
  dataToInsert.type=HouseObjet.type;
  dataToInsert.url=inputFile.value;
  dataToInsert.postalCode=HouseObjet.postalCode;
  dataToInsert.dimension=HouseObjet.dimension;

  requestOptions.body = JSON.stringify(dataToInsert);

  fetch('http://localhost:8080/api/house/insert', requestOptions)
    .then(response => {

      if (!response.ok) {
        throw new Error('Erreur réseau lors de la requête !');
      }

      return response;
    })
    .then(data => {
      createAlert("success", "Les valeurs sont bien insérées dans la database")

    })
    .catch(error => {

      createAlert("danger", "Une erreur s'est produite les valeurs ne sont pas insérées");

    });

  e.preventDefault();

})


/* Alert function */
/******************/
function createAlert(color,message)
{
  alerting.classList.add("alert");
  alerting.classList.add("alert-" + color);
  alerting.textContent = message;
}
