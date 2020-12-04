<template>
    <div class="container flex-fill mx-5 pl-5 mt-3 w-100">
        <div class="row">
            <div class="col-4 align-items-start d-flex flex-column">
                <!-- TODO check imagen -->
                <div class="picture-container no-select">
                    <div class="w-100 d-flex flex-column justify-content-center">
                        <div class="profile-picture-container">
                            <div style="margin-top: 100%;"></div>
                            <img
                                id="profilePic"
                                class="profile-picture rounded-circle"
                                :src="getUrl('profilePics/'+user.profilePictureId)"
                                alt="profile pic"
                            />
                        </div>
                    </div>
                    <div class="picture-overlay d-flex flex-column align-items-center justify-content-end pb-3">
                        <input id="profile-picture-input" style="display: none;" type="file" accept="image/*">
                        <i class="fas fa-pencil-alt"></i>
                    </div>
                </div>
            </div>
            <div class="col-6">
                <div class="container p-0 pt-4 m-0">
                    <!-- TODO:connect form -->
                    <form>
                        <div class="row">
                            <div class="col p-0 m-0">
                                <!-- TODO Connect image function-->
                                <h3>{{ $t('Name') }}
                                    <label for="firstName" class="toggle-readonly">
                                        <img type="button" :src='editPencil' alt="editar"/>
                                    </label>
                                </h3>
                                <input class="form-control mb-3 w-75" id="firstName" name="firstName"
                                       :value="user.firstName" readonly="true"/>
                            </div>
                            <div class="col p-0 m-0">
                                <!-- TODO Connect image function-->
                                <h3>{{ $t('Surname') }}
                                    <label for="surname" class="toggle-readonly">
                                        <img type="button" :src='editPencil' alt="editar"/>
                                    </label>
                                </h3>
                                <input class="form-control mb-3 w-75" name="surname" id="surname" :value="user.surname"
                                       readonly="true"/>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col p-0 m-0">
                                <h3>{{ $t('Phone') }}
                                    <label for="phone" class="toggle-readonly">
                                        <img type="button" :src='editPencil' alt="editar">
                                    </label>
                                </h3>
                                <input class="form-control mb-3 w-75" id="phone" name="phone" :value="user.phone"
                                       readonly="true"/>
                            </div>
                            <div class="col p-0 m-0">
                                <h3>{{ $t('Email') }}
                                    <label for="email" class="toggle-readonly">
                                        <img type="button" :src='editPencil' alt="editar"/>
                                    </label>
                                </h3>
                                <input class="form-control mb-3 w-75" id="email" name="email" :value="user.email"
                                       readonly="true"/>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col p-0 m-0">
                                <h3>{{ $t('Password') }}
                                    <label for="password" class="toggle-readonly">
                                        <img type="button" :src='editPencil' alt="editar"/>
                                    </label>
                                </h3>
                                <input type="password" class="form-control mb-3 w-75" id="password" name="password"
                                       readonly="true"/>
                                <label for="password" class="toggle-visibility">
                                    <img :src='eye' style="display: none;" alt="not visible password">
                                    <img :src='noeye' style="display: none;" alt="visible password">
                                </label>
                            </div>
                            <div class="col p-0 m-0" id="repeat-password-container" style="display: none">
                                <h3>{{ $t('RepeatPassword') }}</h3>
                                <input visible="false" type="password" class="form-control mb-3 w-75"
                                       id="repeatPassword" name="repeatPassword" readonly="true"/>
                                <label for="repeatPassword" class="toggle-visibility">
                                    <img :src='eye' style="display: none;" alt="not visible password">
                                    <img :src='noeye' style="display: none;" alt="visible password">
                                </label>
                            </div>
                        </div>
                        <div class="row justify-content-center align-items-end mt-2">
                            <button type="submit" class="btn btn-info">{{ $t('ConfirmChanges') }}</button>
                        </div>
                    </form>
                    <div class="col-2">
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import noeye from '@/assets/noeye.svg';
import eye from '@/assets/eye.svg';
import editPencil from '@/assets/editPencil.svg';
import {Component, Vue} from 'vue-property-decorator';

@Component
export default class PatientProfile extends Vue {
    private noeye = noeye;
    private eye = eye;
    private editPencil = editPencil;

    private user = new apiTypes.User(1, 'email', 'firstName', 'surname', true, '0000-0000', 1);
}
</script>