Feature: Common SQL queries are stored for quick retrieval.

Scenario Outline: Retrieve years of a domain.
  Given a JDBC connection
  When I send the domain code <a>
  Then I get a list of <b> years

Examples: Domains
  | a    |  b |
  | "QC" | 51 |
  | "QA" | 51 |