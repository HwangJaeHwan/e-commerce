import type UserProfile from "@/entity/user/UserProfile";
import {container} from "tsyringe";
import {defineStore} from "pinia";
import UserRepository from "@/repository/UserRepository";

const USER_REPOSITORY = container.resolve(UserRepository);

export const useProfileStore = defineStore('profile', {
    state: () => ({
        profile: null as UserProfile | null,
    }),
    actions: {
        async fetchProfile() {
            if (!this.profile) {
                const profile = await USER_REPOSITORY.getProfile();
                this.profile = profile;
            }
        },
        setProfile(profile: UserProfile | null) {
            this.profile = profile;
        },
    },
});