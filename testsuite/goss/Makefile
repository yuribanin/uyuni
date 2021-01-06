all: validate

help:
	@echo -e "Please change the domain in 'gossvars.yaml' to match the domain of your Uyuni server and then run 'make'.\n"

validate:
	goss --vars gossvars.yaml v

.PHONY: validate
