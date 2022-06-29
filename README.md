# vault-generate-otp


### Required actions

É necessário ter instalado o vault cli

```zsh
vault server -dev
export VAULT_ADDR='http://127.0.0.1:8200'
vault secrets enable totp
vault token lookup --format=json | jq .data.id
./bootstrap.sh
```
