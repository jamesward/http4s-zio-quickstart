# ZIO + http4s

Run Locally:

```
sbt ~reStart
```

Deploy on Cloud Run:
```
export PROJECT_ID=$(gcloud config get-value project)
./sbt -Ddocker.repo=gcr.io -Ddocker.username=$PROJECT_ID docker:publishLocal
docker push gcr.io/$PROJECT_ID/http4s-zio-quickstart
gcloud beta run deploy --image gcr.io/$PROJECT_ID/http4s-zio-quickstart:0.1.0-SNAPSHOT --region us-central1 http4s-zio-quickstart
```
