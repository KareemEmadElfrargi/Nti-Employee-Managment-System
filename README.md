# Employee Management System

## AuditLogger scoping (ObjectProvider vs. scoped proxy)

`AuditLogger` is `@Scope("prototype")` so every call site gets a fresh, timestamped
logger instance. `EmployeeServiceImpl` is a singleton, so a plain constructor
injection of `AuditLogger` would only ever resolve the prototype **once**, at the
time the singleton is built — every subsequent call would reuse that same first
instance, defeating the point of the prototype scope.

Two standard fixes exist:

1. **`ObjectProvider<AuditLogger>` / `ObjectFactory<AuditLogger>`** — inject a
   provider instead of the bean itself, and call `getObject()` at the point of
   use to pull a brand-new instance from the container each time.
2. **Scoped proxy (`proxyMode = ScopedProxyMode.TARGET_CLASS`)** — inject the
   bean directly, but Spring wraps it in a CGLIB subclass proxy that transparently
   resolves a new target instance from the scope on every method call.

This project uses **`ObjectProvider<AuditLogger>`**, injected into
`EmployeeServiceImpl` and dereferenced with `.getObject()` right before each
`log(...)` call in `addEmployee` and `giveRaise`.

**Why `ObjectProvider` over a scoped proxy here:**

- **Explicit at the call site.** `auditLoggerProvider.getObject()` makes it obvious
  in the code that a new instance is being fetched right now, rather than relying
  on invisible proxy magic behind a field that looks like an ordinary singleton
  reference.
- **No CGLIB subclassing constraints.** A `TARGET_CLASS` scoped proxy requires
  Spring to generate a runtime subclass of `AuditLogger`, which means the class
  can't be `final` and needs a non-private (ideally no-arg-friendly) constructor.
  `ObjectProvider` has no such requirements on the target bean.
- **No proxy overhead/complexity for a plain (non-web) scope.** Scoped proxies
  exist mainly to let a narrower-scoped bean (e.g. `request`/`session`) be
  injected into a wider-scoped one when there's no natural "resolve now" point
  in the code. Here we control the exact call site, so resolving on demand via
  `ObjectProvider` is simpler and just as correct.
- Works identically in the plain `AnnotationConfigApplicationContext` this app
  uses, with no extra scope-proxy infrastructure to set up.

A scoped proxy would have been the better fit if `AuditLogger` were injected into
many singletons as a normal-looking field and we wanted every method call on that
field to transparently target a fresh instance without call sites needing to know
about scoping at all. Since only `EmployeeServiceImpl` needs it, and only at two
specific call sites, `ObjectProvider` keeps the intent visible and avoids proxy
generation entirely.
