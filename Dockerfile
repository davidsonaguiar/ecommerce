# Multi-stage build para otimizar o tamanho da imagem final
FROM maven:3.9.4-eclipse-temurin-17-alpine AS build

# Definir diretório de trabalho
WORKDIR /app

# Copiar arquivos de configuração do Maven primeiro (para cache de dependências)
COPY pom.xml .
COPY src ./src

# Fazer o build da aplicação
RUN mvn clean package -DskipTests

# Estágio final - runtime
FROM eclipse-temurin:17-jre-alpine

# Instalar curl para health checks (opcional)
RUN apk add --no-cache curl

# Criar usuário não-root para segurança
RUN addgroup spring && adduser -D -G spring spring

# Definir diretório de trabalho
WORKDIR /app

# Copiar o JAR da aplicação do estágio de build
COPY --from=build /app/target/*.jar app.jar

# Alterar proprietário dos arquivos
RUN chown -R spring:spring /app

# Mudar para usuário não-root
USER spring

# Expor a porta (Render usa a variável PORT)
EXPOSE ${PORT:-8080}

# Configurar JVM para containers
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC"

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:${PORT:-8080}/actuator/health || exit 1

# Comando para iniciar a aplicação
CMD java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar app.jar