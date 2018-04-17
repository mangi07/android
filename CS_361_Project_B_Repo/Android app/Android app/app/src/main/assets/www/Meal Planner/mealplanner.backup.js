
// Ben: need to bind ajax response to Java function that persists meal plans
//  and add ui capability somewhere to load meal plans through a second Java function


/* This JSON object will ultimately contain all the information for each recipe
returned by the initial recipes/mealplans/generate call. These will be appended
to finalRecipes[i]. Successive calls to different portions of the Spoonacular API
will add information as follows:
1. GET Compute Daily Meal Plan <recipes/mealplans/generate>
   obtains - id, title
2. GET Recipe Information <recipes/{id}/information> 
   obtains - sourceUrl (of outside website), servings, extendedIngredients,
   readyInMinutes, and meal image url, 
3. GET Extract Recipe From Website <recipes/extract> 
   obtains - text, instructions
Once all these things have been appended, we have a finalized recipeObject
that will be referenced as finalRecipes[i].
*/
var finalRecipes = [];
//Recipe Object moved into closure for getWeeklyRequestCallback()
 
/** Function written by: Chris Ferdinandi 3/2/2015
 *  Sourced From: http://gomakethings.com/how-to-get-the-value-of-a-querystring-with-native-javascript/
 *
 *  Use: Will parse a url for "get" queries. Pass 'field' that you want, 'url' is optional
 **/
var getQueryString = function ( field, url ) {
    var href = url ? url : window.location.href;
    var reg = new RegExp( '[?&]' + field + '=([^&#]*)', 'i' );
    var string = reg.exec(href);
    return string ? string[1] : null;
};


/* Spoonacular API Documentation at:
    https://market.mashape.com/spoonacular/recipe-food-nutrition#find-by-nutrients
    https://spoonacular-recipe-food-nutrition-v1.p.mashape.com

    Production Key: bOmaZvaeU8mshuqpe8f0WkZqUCGMp1mxhsnjsnDvVjriaCBS6D 
    Testing Key: DW9XSMsmJ9mshOb8Nu0OUsVY9ry7p1jwSaSjsnB20ChBTkFVg1
*/
var user = "ben2016"; // not used
var key = "DW9XSMsmJ9mshOb8Nu0OUsVY9ry7p1jwSaSjsnB20ChBTkFVg1";
var failure = 0;

/* This function initiates a chain of function calls as follows:
    getWeeklyMealsRequestCallback,
    assignUrlsAndNutrients,
    assignUrlsAndNutRequestCallback,
    assignRecipeInstructions,
    assignRecipeInstRequestCallback
    
    adding properties to a given recipeObject on each RequestCallback.
    Upon completion of this chain of function calls,
    if no particular request was attempted more than 7 times,
    each of the 21 recipeObjects should contain the following 9 properties:
        id, title, sourceUrl, servings, extendedIngredients, readyInMinutes, imageUrl, 
        text, and instructions
*/
function getWeeklyMeals() {
    var req = new XMLHttpRequest();
    var reqData = {};
    /**Get reqData parts here 
     * Each data part is per INDIVIDUAL meal
     **/

    // Ben: swap comments on reqData['targetCalories'] assignment 
    //   to deal with GET request from calorieCalculator.js
    
    //reqData['targetCalories'] = getQueryString('tdee');
    reqData['targetCalories'] = 2000;
    reqData['timeFrame'] = "week";
    
    var reqQuery = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/mealplans/generate?"
    //var reqDataKeys = Object.keys(reqData);
    reqQuery += "targetCalories=" + reqData['targetCalories'];
    reqQuery += "&timeFrame=" + reqData['timeFrame'];
    
    //Open and send query
    req.open("GET", reqQuery , true);
    req.setRequestHeader("X-Mashape-Key", key);
    req.addEventListener('load', function(){
        getWeeklyMealsRequestCallback(req, reqQuery);
    });
    req.send();

}

/* Upon succesful response, 
    adds 21 recipeObjects to the finalRecipes array,
    each recipeObject containing an id and title*/
function getWeeklyMealsRequestCallback(req, reqQuery){
    var reqCount = 0;
    
    if (req.status >= 500)
    {
        //Ensure only 8 requests are made at most
        if(++reqCount >= 8)
        {
            console.log("Sorry, we couldn't get a meal plan.\n");
            failure = 1;
            return;
        }
        req.open("GET", reqQuery , true);
        req.setRequestHeader("X-Mashape-Key", key);
        req.send();
    } else {
        
        var meals = JSON.parse(req.responseText);
        
        var uniqueCallbackGenerator = function(curMeal){
            return function(){

                // 1. GET Compute Daily Meal Plan <recipes/mealplans/generate>
                // obtains - id, title
                var curMealInfo = JSON.parse(curMeal.value);  //Holds id, image type, and title
                
                /* All the data needed for a single recipe will be gathered into this object:
                id, title, */
                var recipeObject = {};                        //Unique recipe object for each function
                recipeObject.day = parseInt(curMeal.day);
                recipeObject.slot = parseInt(curMeal.slot);
                recipeObject.id = curMealInfo.id;
                recipeObject.title = curMealInfo.title;
                finalRecipes.push(recipeObject);              //Pushes unique object to global array
            };
        };
        
        for(var i = 0; i < meals['items'].length; i++){
            //Generates a unique callback for each meal
            var uniqueCallbackFunction = uniqueCallbackGenerator(meals['items'][i]);
            //Calls unique function
            uniqueCallbackFunction();
        }
        
        //finalRecipes should be fully populated with 21 unique recipeObjects
        for (var x = 0; x < finalRecipes.length; x++){
            assignUrlsAndNutrients(finalRecipes[x]);
        }
    }
}

/* Accepts a recipeObject that must contain at least the property id:number */
function assignUrlsAndNutrients(recipeObject){
    console.log("Request for: " + JSON.stringify(recipeObject));
    var reqQuery = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/"
        + recipeObject.id
        + "/information";
    var req = new XMLHttpRequest();
    //Open and send query
    req.open("GET", reqQuery , true);
    req.setRequestHeader("X-Mashape-Key", key);
    
    req.addEventListener('load', function(){
        assignUrlsAndNutRequestCallback(req, reqQuery, recipeObject);
    });
    
    req.send();
}

/*
    Pre-Conditions:
        (1) req is an XMLHttpRequest object,
        (2) reqQuery is of the form 
        "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/<id>/information"
        (3) valid key/value pair "X-Mashape-Key" in req Header is set
        (4) recipeObject contains two properties: id and title
    Post-Conditions:
        (1) Upon succesful response, each recipeObject should contain the following properties:
        id, title, sourceUrl, servings, extendedIngredients, readyInMinutes, imageUrl
    Note: This function is responsible for obtaining the last five properties in this list.
*/
function assignUrlsAndNutRequestCallback(req, reqQuery, recipeObject){
    var reqCount = 0;
    failure = 0;

    if (req.status >= 500){
        //Ensure only 8 requests are made at most
        if(++reqCount >= 8) {
            failure = 1;
            console.log("assignUrl: Unable to get recipe urls and nutrients from meal id.\n");
            return;
        }
        req.open("GET", reqQuery , true);
        req.setRequestHeader("X-Mashape-Key", key);
        req.send();
    } else {
        var response = JSON.parse(req.responseText);
        // parse response here
        recipeObject.sourceUrl = response.sourceUrl;
        recipeObject.servings = response.servings;
        recipeObject.extendedIngredients = response.extendedIngredients;
        recipeObject.readyInMinutes = response.readyInMinutes;
        recipeObject.imageUrl = response.image;
        assignRecipeInstructions(recipeObject);
    }
}

/*
    Pre-Conditions:
        (1) recipeObject.sourceUrl is a valid url
    Post-Conditions:
        (1) Upon succesful response, each recipeObject should contain the following properties:
            id, title, sourceUrl, servings, extendedIngredients, readyInMinutes, imageUrl, 
            text, and instructions
        OR global failure = 1 (through the callback)
    Note: This function is responsible for obtaining the last two properties in this list.
*/
function assignRecipeInstructions(recipeObject){
    var reqQuery = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/extract?"
        /*+ "forceExtraction=false&"*/
        + "url=" + recipeObject.sourceUrl;
        
    var req = new XMLHttpRequest();
    
    req.open("GET", reqQuery, true);
    req.setRequestHeader("X-Mashape-Key", key);
    
    req.addEventListener('load', function(){
        assignRecipeInstRequestCallback(req, reqQuery, recipeObject);
    });
    
    req.send();
}

/*
    Pre-Conditions:
        (1) req is a valid XMLHttpRequest object
        (2) reqQuery is of the following form:
            "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/extract?<url>"
        (3) recipeObject.sourceUrl is a valid url
    Post-Conditions:
        (1) Upon succesful response, each recipeObject should contain the following properties:
            id, title, sourceUrl, servings, extendedIngredients, readyInMinutes, imageUrl, 
            text, and instructions
        OR global failure = 1
    Note: This function is responsible for obtaining the last two properties in this list.
*/
function assignRecipeInstRequestCallback(req, reqQuery, recipeObject) {
        var reqCount = 0;
        failure = 0;

        if (req.status >= 500){
        //Ensure only 8 requests are made at most
        if(++reqCount >= 8) {
            failure = 1;
            console.log("assignRecipeInstructions: Unable to get recipe id =" + recipeObject.id + ".\n");
            return;
        }
            req.open("GET", reqQuery , true);
            req.setRequestHeader("X-Mashape-Key", key);
            req.send();
        } else {
            var response = JSON.parse(req.responseText);
            // parse response here
            //recipeObject.servings = response.servings;
            //recipeObject.extendedIngredients = response.extendedIngredients;
            //recipeObject.readyInMinutes = response.readyInMinutes;
            recipeObject.text = response.text;
            recipeObject.instructions = response.instructions;
            createMealDivs(recipeObject);
        }
}

function createMealDivs(recipeObject){
    /* Needed - better way to display results */
    var newDiv = document.createElement("div");
    newDiv.id = "content";
    newDiv.textContent = JSON.stringify(recipeObject);
    
    document.getElementById("content").appendChild(newDiv);
}

getWeeklyMeals();

console.log("finalRecipes: " + finalRecipes);

/* Needed - Persist finalRecipes in Java */