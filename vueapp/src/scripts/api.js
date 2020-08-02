import apiTypes from "./apiTypes";


export default {
    async getSpecialties(){
        let res = await fetch(process.env.VUE_APP_BASE_API_URL + "specialties");
        if(!res.ok){
            // TODO: better error
            throw new Error();
        }
        let json = await res.json();
        let specialties = [];
        for (const s of json) {
            specialties.push(new apiTypes.StaffSpecialty(s.id,s.name))
        }
        return specialties;
    },
    async getLocalities(){
        let res = await fetch(process.env.VUE_APP_BASE_API_URL + "localities");
        if(!res.ok){
            // TODO: better error
            throw new Error();
        }
        let json = await res.json();
        let localities = [];
        for (const l of json) {
            let country = new apiTypes.Country(l.province.country.id,l.province.country.name);
            let province = new apiTypes.Province(l.province.id,l.province.name,country);
            localities.push(new apiTypes.Locality(l.id,l.name,province));
        }
        return localities;
    }
}