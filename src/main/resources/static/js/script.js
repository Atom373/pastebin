const downloadingDiv = document.getElementById("downloading");

document.getElementById("postForm").addEventListener("submit", function(event) {
    event.preventDefault();

    const formData = new FormData(this);
    
    if (!isValid(formData)) {
		return;
	}

	downloadingDiv.innerHTML="Saving...";

    const serverURL = "http://localhost:8080/";
    fetch(serverURL+"create", {
        method: "POST",
        body: formData
    })
	.then(response => {
		if (!response.ok)
			throw new Error('Error while proceeding the request');
		return response.text();
  })
    .then(hashKey => {
		var copyButton = document.createElement("button");
		if (!downloadingDiv.contains(copyButton)) {
	        copyButton.innerHTML = "Copy URL";
	        copyButton.setAttribute("type", "button");
	        copyButton.classList.add("btn", "btn-primary");
	        downloadingDiv.innerHTML = "";
	        downloadingDiv.appendChild(copyButton);
        }
        copyButton.addEventListener('click', function() {
			const url = serverURL + "get/" + hashKey;
    		navigator.clipboard.writeText(url);
        })
    })
});

function isValid(formData) {
	const titleErrorLabel = document.getElementById("titleErrorLabel");
	const textBlockErrorLabel = document.getElementById("textBlockErrorLabel");
	
	if (formData.get("title").length > 50) {
		titleErrorLabel.style.display = "block";
		return false;
	}
	if (formData.get("text").length == 0) {
		textBlockErrorLabel.style.display = "block";
		return false;
	}
	
	titleErrorLabel.style.display = "none";
	textBlockErrorLabel.style.display = "none";
	return true;
}