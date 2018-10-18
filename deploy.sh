echo "{:user {:deploy-repositories [[\"internal\" {:url \"${ARTIFACTORY_URL}\" :username :env/artifactory_user :password :env/artifactory_pass :sign-releases false}]]}}" > ~/.lein/profiles.clj
lein deploy internal
