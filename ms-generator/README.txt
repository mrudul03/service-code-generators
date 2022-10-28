# Deploy as Google Cloud Run

docker build -t gcr.io/gh-erp-project-3612/ms-generator:latest .
docker run -d -p 8080:8080 gcr.io/gh-erp-project-3612/ms-generator:latest

# Connect docker to google registry. This put your credentials for Cloud Registry into your Docker configuration to authenticate on GCP
gcloud auth configure-docker

# Enable the repository API for your project
gcloud services enable containerregistry.googleapis.com

# Push the image to the google registry
docker push gcr.io/gh-erp-project-3612/ms-generator:latest

# Enable the Cloud Run API.
gcloud services enable run.googleapis.com

# Create a service account for the Cloud Run service. This ensures the respect of the Principle of least privilege.
gcloud iam service-accounts create ms-generator \
	--description="Service account that executes the ms-generator application" \
	--display-name="GCP Cloudrun Backend service account"

# Deploy on Cloud Run (it might take some minutes).
gcloud beta run services replace ms-generator-service.yaml \
	--platform=managed \
	--region=asia-south1



# Allow public access to invoke your service.
gcloud run services add-iam-policy-binding ms-generator \
	--platform=managed \
	--region=asia-south1 \
	--member="allUsers" \
	--role="roles/run.invoker"

--------
Test	


