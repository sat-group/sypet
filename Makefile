# Build docker image for the development environment.
build-dev:
	docker build -t sypet-dev-img --target dev .

# Create docker container for the development environment.
create-dev:
	docker create -it \
	--name sypet-dev \
	--mount type=bind,source=$(PWD),target=/app \
	sypet-dev-img

# Start docker container for the development environment.
start-dev:
	docker start -ai sypet-dev

# Stop docker container for the development environment.
stop-dev:
	docker stop sypet-dev

# Build docker image for the SyPet app.
build-app:
	docker build -t sypet-app-img --target app .

# Run SyPet as a docker app. Usage is `make run-app FILE=<filename>`, where
# <filename> is the argument to SyPet.
run-app:
	docker run --rm \
	--mount type=bind,\
	source=$(shell dirname $(PWD)/$(FILE)),\
	target=/app/$(shell dirname $(FILE)),\
	readonly \
	sypet-app-img $(FILE)

clean:
	docker container rm sypet-dev

veryclean: clean
	docker image rm sypet-dev-img
	docker image rm sypet-app-img

.PHONY: build-dev create-dev start stop \
	build-app run-app \
	clean veryclean
