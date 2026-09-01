## Observability Stacks

1. [OpenTelemetry agent](#opentelemetry-agent)
2. [Prometheus](#prometheus)
3. [Loki](#loki)
4. [Tempo](#tempo)
5. [OTEL Collector](#otel-collector)
6. [Grafana](#grafana)

### OpenTelemetry agent

opentelemetry-javaagent.jar 🔗[Download](https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar)

JVM argument
```shell
-javaagent:\<path>\opentelemetry-javaagent.jar
-Dotel.service.name=<service-name>
-Dotel.traces.exporter=otlp
-Dotel.metrics.exporter=otlp
-Dotel.logs.exporter=otlp
-Dotel.exporter.otlp.protocol=grpc
-Dotel.exporter.otlp.endpoint=http://localhost:14317
-Dotel.instrumentation.micrometer.enabled=true ## micrometer metric
-Dotel.metrics.exemplar.filter=always_on ## enable exemplar  
```

### Prometheus

prometheus-3.13.2 🔗[Download](https://github.com/prometheus/prometheus/releases/download/v3.13.2/prometheus-3.13.2.windows-amd64.zip)

Run
```shell
prometheus.exe --enable-feature=exemplar-storage
```

Config
```yaml
global:
  scrape_interval: 10s # Set the scrape interval to every 15 seconds. Default is every 1 minute.
  evaluation_interval: 15s # Evaluate rules every 15 seconds. The default is every 1 minute.
  # scrape_timeout is set to the global default (10s).

# Control exemplar buffer size in memory
storage:
  exemplars:
    max_exemplars: 100000   # Set to a positive number (default is usually 0/disabled if feature flag is off)

# A scrape configuration containing exactly one endpoint to scrape:
# Here it's Prometheus itself.
scrape_configs:
  - job_name: "otel-collector"
    static_configs:
      - targets: ["localhost:9464"]
        
  # The job name is added as a label `job=<job_name>` to any timeseries scraped from this config.
  - job_name: "prometheus"
    # metrics_path defaults to '/metrics'
    # scheme defaults to 'http'.
    static_configs:
      - targets: ["localhost:9090"]
       # The label name is added as a label `label_name=<label_value>` to any timeseries scraped from this config.
        labels:
          app: "prometheus"
```

### Loki

loki-3.7.6 🔗[Download](https://github.com/grafana/loki/releases/download/v3.7.6/loki-windows-amd64.exe.zip)

Run
```shell
loki-windows-amd64.exe --config.file=loki.yaml
```

http://localhost:3100/ready

Config
```yaml
# loki local configuration example: https://raw.githubusercontent.com/grafana/loki/main/cmd/loki/loki-local-config.yaml
# loki configuration examples: https://grafana.com/docs/loki/latest/configure/examples/configuration-examples/
auth_enabled: false

server:
  http_listen_port: 3100
  grpc_listen_port: 9096
  log_level: info

common:
  path_prefix: ./tmp
  storage:
    filesystem:
      chunks_directory: ./tmp/chunks
      rules_directory: ./tmp/rules
  replication_factor: 1
  ring:
    kvstore:
      store: inmemory

schema_config:
  configs:
    - from: 2024-01-01
      store: tsdb
      object_store: filesystem
      schema: v13
      index:
        prefix: index_
        period: 24h

limits_config:
  allow_structured_metadata: true
  retention_period: 168h
  reject_old_samples: true
```

### Tempo

tempo-3.0.2 🔗[Download](https://github.com/grafana/tempo/releases/download/v3.0.2/tempo_3.0.2_windows_amd64.tar.gz)

Run
```shell
tempo.exe -config.file tempo.yaml
```

http://localhost:3200/ready

Config
```yaml
# https://grafana.com/docs/tempo/latest/configuration/#configure-tempo

server:
  http_listen_address: 0.0.0.0
  http_listen_port: 3200

distributor:
  receivers:
    otlp:
      protocols:
        grpc:
          endpoint: "0.0.0.0:4317"
        http:
          endpoint: "0.0.0.0:4318"

storage:
  trace:
    backend: local
    local:
      path: ./tmp/traces
    wal:
      path: ./tmp/wal
```

### OTEL Collector

otelcol-contrib-0.158.0 🔗[Download](https://github.com/open-telemetry/opentelemetry-collector-releases/releases/download/v0.158.0/otelcol-contrib_0.158.0_windows_amd64.tar.gz)

Run

```shell
otelcol-contrib.exe --config collector-config.yaml
```
Otel collector configuration example: https://opentelemetry.io/docs/collector/configuration/

Config

```yaml
receivers:
  otlp:
    protocols:
      grpc:
        endpoint: 0.0.0.0:14317
      http:
        endpoint: 0.0.0.0:14318

processors:
  batch:
    send_batch_size: 8192
    timeout: 5s
    send_batch_max_size: 10240

exporters:
  otlphttp/loki:
    endpoint: http://localhost:3100/otlp/
    
  prometheus:
    endpoint: 0.0.0.0:9464
    enable_open_metrics: true
    send_timestamps: true
    
  otlp/tempo:
    endpoint: 0.0.0.0:4317
    tls:
      insecure: true            
              
service:
  pipelines:
    logs:
      receivers: [otlp]
      exporters: [otlphttp/loki]
    metrics:
      receivers: [otlp]
      processors: [batch]
      exporters: [prometheus]
    traces:
      receivers: [otlp]
      exporters: [otlp/tempo]
```

### Grafana

grafana-13.1.3 🔗[Download](https://dl.grafana.com/grafana-enterprise/release/13.1.3/grafana-enterprise_13.1.3_31135815010_windows_amd64.tar.gz)

Run
```shell
grafana.exe server
```