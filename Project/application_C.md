# COMP 272/400C — Term Project

## Application C — Web Page Cache for a Proxy Server

This sheet describes one of the three proposed applications. Read it together with the main project handout, which describes the five stages, the deliverables, and the grading.

**Starter code:** project `app-c-cache` — interface `PageCache`, skeletons `Solution1` and `Solution2`, `DatasetReader`, the tests `PageCacheTest`, and extra policy tests `LruPolicyTest` and `FifoPolicyTest`.

---

### Scenario

A proxy server sits between users and Wikipedia. It keeps recently requested pages in memory, up to a fixed **capacity** (a number of pages):

- If a requested page is in the cache, it is a **hit**: the page is returned immediately.
- Otherwise, it is a **miss**: the page is fetched from Wikipedia (slow) and inserted into the cache. If the cache is full, one page is **evicted** first, according to an **eviction policy**:
  - **LRU** (least recently used): evict the page that was requested least recently;
  - **FIFO** (first in, first out): evict the page that entered the cache first;
  - **LFU** (least frequently used): evict the page requested the fewest times.

The goals are a high **hit rate** and a very small cost per request, because the proxy serves millions of requests per hour.

### Dataset

| Source | Content | Format |
|---|---|---|
| [Wikimedia pageview dumps](https://dumps.wikimedia.org/other/pageviews/) | One file per hour. Each line gives a project (e.g. `en` for English Wikipedia), a page title, and the number of times it was viewed during that hour. One hour has millions of lines. | Plain text compressed with gzip (`.gz`), space-separated: `domain_code page_title count_views total_response_size` |

**Building the request stream.** The files give counts, not the order of the requests. Build a stream of requests as follows, and document your method in the report:

1. Keep the English Wikipedia lines (`en` and `en.m`).
2. Turn each line `(page, count)` into `count` requests for that page.
3. **Shuffle** the requests with a fixed random seed. Optionally, concatenate several consecutive hours, so that the popular pages change over time as they do in reality.

You do not need real page contents: store a small placeholder object (e.g. the title) as the page.

**Tips:**

- Read the `.gz` files directly in Java with `new GZIPInputStream(new FileInputStream(file))`.
- One hour of English pageviews already gives tens of millions of requests. Develop with a sample (e.g. the first 100,000 lines).

### Functional requirements

The interface `PageCache` in the starter code is the exact specification; this table summarizes it. Each solution receives its capacity in its constructor.

| Operation | Description |
|---|---|
| `get(page)` | Return the cached page (a hit), or `null` (a miss). On a hit, update whatever the policy needs (recency, frequency). |
| `put(page, content)` | Insert the page, or replace its content if it is already present. If the page is new and the cache is full, first evict one page according to the policy. |
| `contains(page)` | Is the page in the cache? (Not counted as a hit or a miss; does not change the policy's information.) |
| `size()`, `capacity()` | Current number of pages, and the maximum. |
| `hits()`, `misses()`, `evictions()`, `hitRate()` | Statistics. |

### Non-functional requirements (targets)

Measured on the real request stream, on your machine, with at most **2 GB** of heap (`-Xmx2g`).

| ID | Requirement | Target | Rationale |
|---|---|---|---|
| C-N1 | Average time of a request (`get`, plus `put` on a miss), capacity 10⁶ pages | ≤ 1 µs | The cache must be much faster than fetching the page (milliseconds). |
| C-N2 | Throughput, capacity 10⁶ pages | ≥ 1,000,000 requests per second on one thread | A busy proxy serves millions of requests per hour, in bursts. |
| C-N3 | Scalability | The average time per request stays within 2× of its value at capacity 10³, up to capacity 10⁶ | The capacity will grow as the server gets more memory. |
| C-N4 | Memory overhead of the cache structure (excluding page contents) | ≤ 100 bytes per cached page | Memory should hold pages, not bookkeeping. |
| C-N5 | Hit rate at capacity 10⁵ on the real stream No fixed target: report it for both solutions | The hit rate is the purpose of the cache: a faster design with a much lower hit rate may be worse overall. |

You may refine these targets or add your own; justify each change in your proposal.

### Workloads to test

- **Capacities:** 10², 10³, 10⁴, 10⁵, 10⁶ pages.
- **Real stream:** built from the pageview dumps as described above.
- **Uniform stream:** the same pages, each equally likely (no popularity). This shows how much the hit rate owes to popularity.
- **Scan:** a burst of requests to many pages that are each requested only once, inserted in the middle of the real stream. How quickly does each policy recover?

**Note:** A design with O(n) cost per request (e.g. a linked list without an index) cannot process tens of millions of requests at capacity 10⁶ in reasonable time. For such a design, use smaller capacities and shorter streams, explain why, and **extrapolate** from your measurements.

### Suggested solution pairs

Each solution may come from your team, from class, from a library, or from another source, as long as you explain its implementation and expected performance (see Section 4.3 of the main handout). For example, `java.util.LinkedHashMap` can be configured as an LRU cache: if you use it, describe how it works internally.

| Solution 1 | Solution 2 | Main trade-off to investigate |
|---|---|---|
| LRU: hash map + doubly linked list | LRU: doubly linked list only (linear search) | Why the index matters: O(1) vs. O(n) per request |
| LRU with a chaining hash map | LRU with a linear-probing hash map | Same policy, different index; deletions in open addressing |
| LRU: hash map + doubly linked list | FIFO: hash map + queue | Same structures, different policy: hit rate vs. simplicity |
| LRU: hash map + doubly linked list | LFU: hash map + binary heap | Recency vs. frequency; O(1) vs. O(log n) per request |
| LRU: hash map + doubly linked list | LRU: Red-Black tree keyed by last access time | O(1) vs. O(log n) recency updates |

### Questions your analysis should answer

- How does the hit rate grow with capacity? Where is the point of diminishing returns?
- Do your two solutions have the same hit rate? If yes, only **speed and memory** separate them. If no, explain why.
- How does eviction affect your hash table? With open addressing, how did you handle deletions, and what did it cost?
- Combine speed and hit rate: if a miss costs 20 ms (fetching from Wikipedia), which solution gives the lowest **average time per request** overall?
