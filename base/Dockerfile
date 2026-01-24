# Base Dockerfile for Java applications
# This image will be used by both app-public and app-internal

ARG JAVA_VERSION=17

FROM eclipse-temurin:${JAVA_VERSION}-jdk-alpine AS base

# Install common utilities
RUN apk add --no-cache \
    curl \
    bash \
    tzdata \
    wget

# Set timezone
ENV TZ=Europe/Sofia
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Create app directory
RUN mkdir -p /app

# Set working directory
WORKDIR /app

# Add a non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

LABEL maintainer="veso@fmi.plovdiv.bg"
LABEL description="Base Java 17 image for Campus Management System"