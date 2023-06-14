SHELL := /bin/bash
#################################################################################################
##       Makefile for Altus Performance Platform / Stewardship Environment rebuilds       ##
#################################################################################################

provision = ./infra/scripts/provisioner.sh

#########################################################################################################
##       Tasks to plan, create & destroy Altus Performance Platform / Stewardship resources     ##
#########################################################################################################

component-plan:
	$(provision) component-plan $(env) $(component)

component-build:
	$(provision) component-build $(env) $(component)

component-destroy:
	$(provision) component-destroy $(env) $(component)