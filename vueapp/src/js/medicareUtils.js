function getUrl(url) {
    if(typeof url =='undefined'){
        return process.env.BASE_URL;
    }else{
        return process.env.BASE_URL + url;
    }
}

export default {
    getUrl:getUrl
};