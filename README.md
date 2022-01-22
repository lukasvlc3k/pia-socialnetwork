# pia-socialnetwork

KIV/PIA semestral work - the social network

Start postgres server (docker)
docker run --name pg -ePOSTGRES_PASSWORD=HEun3RGgEYwknRuk4adh4ZKW -ePOSTGRES_USER=pia -ePGDATA=/var/lib/postgresql/data/pgdata -d --rm --shm-size=256MB -p5432:5432 -v pia-pgdata:/var/lib/postgresql/data postgres
