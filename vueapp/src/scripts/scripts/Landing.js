$(document).ready(function(){
/*
    var faded = false;

    $(document).scroll(function(){
        var actualTop = window.pageYOffset || document.documentElement.scrollTop;
        if(actualTop > 100 && faded === false)
        {
            $('.header').animate({
                "opacity" : "-=0.05"
            }, 250);
            faded = true;
        }
        else if(faded === true && actualTop < 100)
        {
            $('.header').animate({
                "opacity" : "+=0.05"
            }, 250);
            faded = false;
        }
    });
*/
    $(".shareSocial").jsSocials({
        showLabel: false,
        showCount: false,
        shares: [{
            share: "facebook",
            logo: logoUrls["facebook"]
        }, {
            share: "twitter",
            logo: logoUrls["twitter"]
        },{
            share: "linkedin",
            logo: logoUrls["linkedin"]
        },{
            share: "pinterest",
            logo: logoUrls["pinterest"]
        }]
    });
});