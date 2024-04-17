$(document).ready(function () {
    var path = window.location.pathname;
    var parts = path.split("/");
    var secondLastPart = parts[parts.length - 2];
    var themeBuilderIndex = parts.indexOf("wknd");
    var siteCode = parts[themeBuilderIndex + 1];

    // For Template Page URL Assigning
    if (siteCode === "settings") {
        siteCode = secondLastPart;
    }

    console.log(siteCode);

    $.ajax({
        url:
            "http://139.59.42.175:3001/api/get-color-by-sitecode?siteCode=" + siteCode,
        type: "GET",
        dataType: "json",
        success: function (data) {
            console.log(data);
            const obj = data;
            const myJSON = JSON.stringify(obj);
            console.log(myJSON);
            let root = document.documentElement;

            root.style.setProperty(" --brandPrimary", obj.BtnBgcolor);
            root.style.setProperty("--button-border-radius", obj.BtnBorderRadius);
            root.style.setProperty("--button-text-color", obj.BtnTextColor);
            root.style.setProperty("--brandPrimary", obj.BtnBgcolor);
            root.style.setProperty("--textColor", obj.fontColor);
            root.style.setProperty("--font-size", obj.fontSize);
            root.style.setProperty("--title-font-color", obj.titleColor);
            root.style.setProperty("--title-font-size", obj.titleFontSize);
            root.style.setProperty("--bodyBackground", obj.backgroundColor);
        },
        error: function () {
            console.log("Error");
        },
    });
});